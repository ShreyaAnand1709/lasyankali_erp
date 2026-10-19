import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  Link,
  useLocation,
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  deactivateTeacher,
  getPerson,
  listPeople,
  savePerson,
} from "../api/people";
import type {
  Person,
  PersonKind,
  PersonPayload,
} from "../api/people";
import "../css/StudentManagement.css";

type Field = {
  key: string;
  label: string;
  type?: string;
  required?: boolean;
  options?: string[];
};

const commonFields: Field[] = [
  { key: "firstName", label: "First name", required: true },
  { key: "lastName", label: "Last name", required: true },
  { key: "email", label: "Email", type: "email", required: true },
  {
    key: "mobileNumber",
    label: "Mobile number",
    type: "tel",
    required: true,
  },
];

const accountFields: Field[] = [
  { key: "username", label: "Username", required: true },
  {
    key: "password",
    label: "Password",
    type: "password",
    required: true,
  },
];

const teacherFields: Field[] = [
  { key: "employeeCode", label: "Employee code", required: true },
  { key: "specialization", label: "Specialization" },
  { key: "qualification", label: "Qualification" },
  {
    key: "yearsOfExperience",
    label: "Years of experience",
    type: "number",
  },
  {
    key: "joiningDate",
    label: "Joining date",
    type: "date",
    required: true,
  },
];

const parentFields: Field[] = [
  { key: "occupation", label: "Occupation" },
  { key: "address", label: "Address" },
];

const statusField: Field = {
  key: "status",
  label: "Profile status",
  required: true,
  options: ["ACTIVE", "INACTIVE"],
};

function errorMessage(exception: unknown) {
  return exception instanceof Error
    ? exception.message
    : "Unable to complete the request.";
}

export default function PeopleManagement({
  kind,
}: {
  kind: PersonKind;
}) {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const root = `/dashboard/admin/${kind}`;
  const isTeacher = kind === "teachers";
  const singular = isTeacher ? "teacher" : "parent";
  const plural = isTeacher ? "Teachers" : "Parents";
  const idField = isTeacher ? "teacherId" : "parentId";

  const isCreate = location.pathname.endsWith("/new");
  const isEdit = Boolean(id) && location.pathname.endsWith("/edit");
  const isList = !id && !isCreate;

  const editableFields = [
    ...commonFields,
    ...(isTeacher ? teacherFields : parentFields),
    ...(isTeacher && !isCreate ? [statusField] : []),
  ];

  const [rows, setRows] = useState<Person[]>([]);
  const [person, setPerson] = useState<Person | null>(null);
  const [form, setForm] = useState<Record<string, string>>({});
  const [input, setInput] = useState("");
  const [keyword, setKeyword] = useState("");
  const [attempt, setAttempt] = useState(0);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    async function load() {
      setLoading(true);
      setError("");
      setNotice("");
      setPerson(null);
      setForm({});

      try {
        if (isCreate) {
          setForm({});
        } else if (id) {
          const result = await getPerson(kind, id, controller.signal);

          if (!controller.signal.aborted) {
            setPerson(result);

            const values: Record<string, string> = {};

            Object.entries(result).forEach(([key, value]) => {
              values[key] = value == null ? "" : String(value);
            });

            setForm(values);
          }
        } else {
          const result = await listPeople(
            kind,
            keyword,
            controller.signal,
          );

          if (!controller.signal.aborted) {
            setRows(result);
          }
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(errorMessage(exception));
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void load();
    return () => controller.abort();
  }, [kind, id, isCreate, isEdit, keyword, attempt]);

  function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setKeyword(input.trim());
    setAttempt((previous) => previous + 1);
  }

  async function handleSave(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (busy) return;

    setBusy(true);
    setError("");
    setNotice("");

    try {
      const fields = [
        ...editableFields,
        ...(isCreate ? accountFields : []),
      ];

      const payload: PersonPayload = {};

      for (const field of fields) {
        const raw = form[field.key] ?? "";
        const value = field.type === "password" ? raw : raw.trim();

        if (field.type === "number" && value !== "") {
          const number = Number(value);

          if (!Number.isInteger(number) || number < 0) {
            throw new Error(
              "Years of experience must be a non-negative whole number.",
            );
          }

          payload[field.key] = number;
        } else {
          payload[field.key] = value === "" ? null : value;
        }
      }

      await savePerson(kind, payload, isCreate ? undefined : id);

      if (isCreate) {
        setForm({});
        setNotice(
          `${isTeacher ? "Teacher" : "Parent"} created successfully. ` +
          "Return to the list to view the new record.",
        );
      } else {
        navigate(`${root}/${id}`, { replace: true });
      }
    } catch (exception) {
      setError(errorMessage(exception));
    } finally {
      setBusy(false);
    }
  }

  async function handleDeactivate() {
    if (!person || busy || !isTeacher) return;

    if (!window.confirm(
      "Deactivate this teacher profile? " +
      "This does not disable their login account.",
    )) {
      return;
    }

    setBusy(true);
    setError("");

    try {
      await deactivateTeacher(String(person.teacherId));
      setPerson({ ...person, status: "INACTIVE" });
      setNotice("Teacher profile deactivated.");
    } catch (exception) {
      setError(errorMessage(exception));
    } finally {
      setBusy(false);
    }
  }

  function renderField(field: Field) {
    return (
      <div className="student-field" key={field.key}>
        <label htmlFor={`person-${field.key}`}>
          {field.label}{field.required ? " *" : ""}
        </label>

        {field.options ? (
          <select
            id={`person-${field.key}`}
            required={field.required}
            value={form[field.key] ?? ""}
            onChange={(event) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: event.target.value,
              }))
            }
          >
            <option value="">Select</option>
            {field.options.map((option) => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        ) : (
          <input
            id={`person-${field.key}`}
            type={field.type ?? "text"}
            required={field.required}
            min={field.type === "number" ? 0 : undefined}
            step={field.type === "number" ? 1 : undefined}
            autoComplete={
              field.type === "password" ? "new-password" : undefined
            }
            value={form[field.key] ?? ""}
            onChange={(event) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: event.target.value,
              }))
            }
          />
        )}
      </div>
    );
  }

  return (
    <section className="student-module">
      <div className="student-heading">
        <div>
          <p className="admin-eyebrow">Academy management</p>
          <h1>
            {isList
              ? plural
              : isCreate
                ? `Add ${singular}`
                : isEdit
                  ? `Edit ${singular}`
                  : `${isTeacher ? "Teacher" : "Parent"} details`}
          </h1>
        </div>

        <Link
          className="student-button"
          to={isList ? `${root}/new` : root}
        >
          {isList ? `+ Add ${singular}` : `Back to ${kind}`}
        </Link>
      </div>

      {notice && (
        <p className="student-notice" role="status">{notice}</p>
      )}

      {error && (
        <div className="student-error" role="alert">
          <p>{error}</p>
          <Link to="/login">Go to login</Link>
          {!isCreate && !isEdit && (
            <button
              type="button"
              onClick={() => setAttempt((previous) => previous + 1)}
            >
              Retry
            </button>
          )}
        </div>
      )}

      {loading ? (
        <p role="status">Loading…</p>
      ) : isList ? (
        <>
          <form className="student-search" onSubmit={handleSearch}>
            <label className="student-field">
              Search {kind}
              <input
                type="search"
                value={input}
                placeholder="Name or keyword"
                onChange={(event) => setInput(event.target.value)}
              />
            </label>

            <button className="student-button" type="submit">
              Search
            </button>

            <button
              className="student-button student-secondary"
              type="button"
              onClick={() => {
                setInput("");
                setKeyword("");
                setAttempt((previous) => previous + 1);
              }}
            >
              Clear
            </button>
          </form>

          {!error && (
            <div className="student-table-wrapper">
              <table className="student-table">
                <caption>
                  {keyword ? `Results for “${keyword}”` : plural}
                  {" · "}{rows.length} records
                </caption>

                <thead>
                  <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email</th>
                    <th scope="col">
                      {isTeacher ? "Status" : "Mobile"}
                    </th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {rows.map((row) => (
                    <tr key={String(row[idField])}>
                      <td>
                        {[row.firstName, row.lastName]
                          .filter(Boolean).join(" ") || row.username}
                      </td>
                      <td>{row.username}</td>
                      <td>{row.email || "—"}</td>
                      <td>
                        {(isTeacher ? row.status : row.mobileNumber) || "—"}
                      </td>
                      <td>
                        <div className="student-actions">
                          <Link to={`${root}/${row[idField]}`}>View</Link>
                          <Link to={`${root}/${row[idField]}/edit`}>
                            Edit
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}

                  {rows.length === 0 && (
                    <tr><td colSpan={5}>No records found.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </>
      ) : isCreate || isEdit ? (
        (isCreate || person) && (
          <form className="student-form" onSubmit={handleSave}>
            <fieldset disabled={busy}>
              <legend>Profile details</legend>
              <div className="student-form-grid">
                {editableFields.map(renderField)}
              </div>
            </fieldset>

            {isCreate && (
              <fieldset disabled={busy}>
                <legend>Account credentials</legend>
                <div className="student-form-grid">
                  {accountFields.map(renderField)}
                </div>
              </fieldset>
            )}

            <div className="student-actions">
              <button
                className="student-button"
                type="submit"
                disabled={busy}
              >
                {busy ? "Saving…" : isCreate ? "Create" : "Save changes"}
              </button>
              <Link to={id ? `${root}/${id}` : root}>Cancel</Link>
            </div>
          </form>
        )
      ) : person ? (
        <div className="student-details">
          <dl className="student-details-grid">
            {[
              { key: idField, label: "Profile ID" },
              { key: "username", label: "Username" },
              ...editableFields,
            ].map((field) => (
              <div key={field.key}>
                <dt>{field.label}</dt>
                <dd>{String(person[field.key] ?? "") || "—"}</dd>
              </div>
            ))}
          </dl>

          <div className="student-actions">
            <Link className="student-button" to={`${root}/${id}/edit`}>
              Edit {singular}
            </Link>

            {isTeacher && person.status !== "INACTIVE" && (
              <button
                className="student-button student-danger"
                type="button"
                disabled={busy}
                onClick={() => void handleDeactivate()}
              >
                {busy ? "Deactivating…" : "Deactivate profile"}
              </button>
            )}
          </div>
        </div>
      ) : null}
    </section>
  );
}