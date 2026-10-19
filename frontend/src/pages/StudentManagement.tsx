import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  Link,
  useLocation,
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  createStudent,
  deactivateStudent,
  getStudent,
  getStudents,
  updateStudent,
} from "../api/students";
import type { Student, StudentPayload } from "../api/students";
import "../css/StudentManagement.css";

const ROOT = "/dashboard/admin/students";

type Field = {
  key: string;
  label: string;
  type?: string;
  required?: boolean;
  options?: string[];
};

const studentFields: Field[] = [
  { key: "firstName", label: "First name", required: true },
  { key: "lastName", label: "Last name", required: true },
  { key: "email", label: "Email", type: "email", required: true },
  { key: "mobileNumber", label: "Mobile number", type: "tel", required: true },
  { key: "admissionNumber", label: "Admission number", required: true },
  {
    key: "gender",
    label: "Gender",
    required: true,
    options: ["MALE", "FEMALE", "OTHER"],
  },
  { key: "dateOfBirth", label: "Date of birth", type: "date" },
  {
    key: "joiningDate",
    label: "Joining date",
    type: "date",
    required: true,
  },
  { key: "photoUrl", label: "Photo URL", type: "url" },
];

const accountFields: Field[] = [
  { key: "username", label: "Student username", required: true },
  {
    key: "password",
    label: "Student password",
    type: "password",
    required: true,
  },
];

const parentFields: Field[] = [
  { key: "parentFirstName", label: "Parent first name", required: true },
  { key: "parentLastName", label: "Parent last name", required: true },
  { key: "parentUsername", label: "Parent username", required: true },
  {
    key: "parentEmail",
    label: "Parent email",
    type: "email",
    required: true,
  },
  {
    key: "parentPassword",
    label: "Parent password",
    type: "password",
    required: true,
  },
  {
    key: "parentMobileNumber",
    label: "Parent mobile number",
    type: "tel",
    required: true,
  },
  { key: "occupation", label: "Parent occupation" },
  { key: "address", label: "Address" },
];

const statusField: Field = {
  key: "status",
  label: "Profile status",
  required: true,
  options: ["ACTIVE", "INACTIVE"],
};

function messageFrom(exception: unknown) {
  return exception instanceof Error
    ? exception.message
    : "Something went wrong. Please try again.";
}

export default function StudentManagement() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const isCreate = location.pathname.endsWith("/new");
  const isEdit = Boolean(id) && location.pathname.endsWith("/edit");
  const isList = !id && !isCreate;

  const [students, setStudents] = useState<Student[]>([]);
  const [student, setStudent] = useState<Student | null>(null);
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
      setStudent(null);
      setForm({});

      try {
        if (isCreate) {
          setForm({});
        } else if (id) {
          const result = await getStudent(id, controller.signal);

          if (!controller.signal.aborted) {
            // Refuse editing against the old DTO rather than erase DOB.
            if (isEdit && result.dateOfBirth === undefined) {
              throw new Error(
                "Update StudentResponseDTO and StudentMapper to include dateOfBirth before editing.",
              );
            }

            setStudent(result);

            const values: Record<string, string> = {};

            for (const field of [...studentFields, statusField]) {
              const value = result[field.key as keyof Student];
              values[field.key] = value == null ? "" : String(value);
            }

            setForm(values);
          }
        } else {
          const result = await getStudents(controller.signal, keyword);

          if (!controller.signal.aborted) {
            setStudents(result);
          }
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(messageFrom(exception));
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void load();
    return () => controller.abort();
  }, [id, isCreate, isEdit, keyword, attempt]);

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

    const fields = isCreate
      ? [...studentFields, ...accountFields, ...parentFields]
      : [...studentFields, statusField];

    const payload: StudentPayload = {};

    for (const field of fields) {
      const raw = form[field.key] ?? "";
      const value = field.type === "password" ? raw : raw.trim();
      payload[field.key] = value === "" ? null : value;
    }

    try {
      if (isCreate) {
        await createStudent(payload);

        setForm({});
        setNotice(
          "Student and parent accounts created successfully. Return to the list to view the student.",
        );
      } else if (id) {
        await updateStudent(id, payload);
        navigate(`${ROOT}/${id}`, { replace: true });
      }
    } catch (exception) {
      setError(messageFrom(exception));
    } finally {
      setBusy(false);
    }
  }

  async function handleDeactivate() {
    if (!student || busy) return;

    const confirmed = window.confirm(
      `Deactivate ${student.firstName ?? student.username}? ` +
      "This changes their student profile to INACTIVE. " +
      "It does not disable their login account.",
    );

    if (!confirmed) return;

    setBusy(true);
    setError("");

    try {
      await deactivateStudent(String(student.studentId));
      setStudent({ ...student, status: "INACTIVE" });
      setNotice("Student profile deactivated successfully.");
    } catch (exception) {
      setError(messageFrom(exception));
    } finally {
      setBusy(false);
    }
  }

  function renderFields(fields: Field[]) {
    return fields.map((field) => (
      <div className="student-field" key={field.key}>
        <label htmlFor={`student-${field.key}`}>
          {field.label}{field.required ? " *" : ""}
        </label>

        {field.options ? (
          <select
            id={`student-${field.key}`}
            value={form[field.key] ?? ""}
            required={field.required}
            disabled={busy}
            onChange={(event) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: event.target.value,
              }))
            }
          >
            <option value="">Select</option>
            {field.options.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        ) : (
          <input
            id={`student-${field.key}`}
            type={field.type ?? "text"}
            value={form[field.key] ?? ""}
            required={field.required}
            disabled={busy}
            autoComplete={
              field.type === "password" ? "new-password" : undefined
            }
            onChange={(event) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: event.target.value,
              }))
            }
          />
        )}
      </div>
    ));
  }

  const title = isCreate
    ? "Add student"
    : isEdit
      ? "Edit student"
      : isList
        ? "Students"
        : "Student details";

  return (
    <section className="student-module">
      <div className="student-heading">
        <div>
          <p className="admin-eyebrow">Academy management</p>
          <h1>{title}</h1>
        </div>

        <Link className="student-button" to={isList ? `${ROOT}/new` : ROOT}>
          {isList ? "+ Add student" : "Back to students"}
        </Link>
      </div>

      {notice && <p className="student-notice" role="status">{notice}</p>}

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
              Search students
              <input
                type="search"
                placeholder="Name or keyword"
                value={input}
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
                  {keyword ? `Results for “${keyword}”` : "Active students"}
                  {" · "}{students.length} records
                </caption>

                <thead>
                  <tr>
                    <th scope="col">Admission no.</th>
                    <th scope="col">Student</th>
                    <th scope="col">Email</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {students.map((item) => (
                    <tr key={item.studentId}>
                      <td>{item.admissionNumber || "—"}</td>
                      <td>
                        {[item.firstName, item.lastName]
                          .filter(Boolean).join(" ") || item.username}
                      </td>
                      <td>{item.email || "—"}</td>
                      <td>{item.status}</td>
                      <td>
                        <div className="student-actions">
                          <Link to={`${ROOT}/${item.studentId}`}>View</Link>
                          <Link to={`${ROOT}/${item.studentId}/edit`}>Edit</Link>
                        </div>
                      </td>
                    </tr>
                  ))}

                  {students.length === 0 && (
                    <tr>
                      <td colSpan={5}>No students found.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </>
      ) : isCreate || isEdit ? (
        (isCreate || student) && (
          <form className="student-form" onSubmit={handleSave}>
            <fieldset disabled={busy}>
              <legend>Student details</legend>
              <div className="student-form-grid">
                {renderFields(studentFields)}
                {isEdit && renderFields([statusField])}
              </div>
            </fieldset>

            {isCreate && (
              <>
                <fieldset disabled={busy}>
                  <legend>Student account</legend>
                  <div className="student-form-grid">
                    {renderFields(accountFields)}
                  </div>
                </fieldset>

                <fieldset disabled={busy}>
                  <legend>New parent account</legend>
                  <p className="student-hint">
                    This workflow creates a new parent. Existing-parent
                    selection is not supported yet.
                  </p>
                  <div className="student-form-grid">
                    {renderFields(parentFields)}
                  </div>
                </fieldset>
              </>
            )}

            <div className="student-actions">
              <button className="student-button" type="submit" disabled={busy}>
                {busy ? "Saving…" : isCreate ? "Create student" : "Save changes"}
              </button>

              <Link to={id ? `${ROOT}/${id}` : ROOT}>Cancel</Link>
            </div>
          </form>
        )
      ) : student ? (
        <div className="student-details">
          <dl className="student-details-grid">
            {[
              { key: "studentId", label: "Student ID" },
              { key: "username", label: "Username" },
              ...studentFields,
              statusField,
            ].map((field) => (
              <div key={field.key}>
                <dt>{field.label}</dt>
                <dd>
                  {String(student[field.key as keyof Student] ?? "") || "—"}
                </dd>
              </div>
            ))}
          </dl>

          <div className="student-actions">
            <Link className="student-button" to={`${ROOT}/${id}/edit`}>
              Edit student
            </Link>

            {student.status !== "INACTIVE" && (
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