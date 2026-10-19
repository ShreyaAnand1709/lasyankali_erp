import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  Link,
  useLocation,
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  changeBatchMember,
  deactivateBatch,
  getBatch,
  getBatches,
  getBatchMembers,
  saveBatch,
} from "../api/batches";
import type { Batch, BatchMember, BatchPayload } from "../api/batches";

import { getStudents } from "../api/students";
import { listPeople } from "../api/people";
import "../css/StudentManagement.css";
import "../css/BatchManagement.css";

const ROOT = "/dashboard/admin/batches";

type Choice = {
  id: string;
  label: string;
};

type Field = {
  key: string;
  label: string;
  type?: string;
  options?: string[];
};

const fields: Field[] = [
  { key: "batchName", label: "Batch name" },
  {
    key: "discipline",
    label: "Discipline",
    options: ["BHARATANATYAM", "VOCAL", "TABALA", "YOGA"],
  },
  {
    key: "level",
    label: "Level",
    options: ["BEGINNER", "INTERMEDIATE", "ADVANCED"],
  },
  { key: "startTime", label: "Start time", type: "time" },
  { key: "endTime", label: "End time", type: "time" },
  { key: "capacity", label: "Capacity", type: "number" },
];

function errorMessage(exception: unknown) {
  return exception instanceof Error
    ? exception.message
    : "Unable to complete the request.";
}

export default function BatchManagement() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const isCreate = location.pathname.endsWith("/new");
  const isEdit = Boolean(id) && location.pathname.endsWith("/edit");
  const isList = !id && !isCreate;

  const [batches, setBatches] = useState<Batch[]>([]);
  const [batch, setBatch] = useState<Batch | null>(null);
  const [form, setForm] = useState<Record<string, string>>({});

  const [students, setStudents] = useState<BatchMember[]>([]);
  const [teachers, setTeachers] = useState<BatchMember[]>([]);
  const [studentChoices, setStudentChoices] = useState<Choice[]>([]);
  const [teacherChoices, setTeacherChoices] = useState<Choice[]>([]);
  const [studentId, setStudentId] = useState("");
  const [teacherId, setTeacherId] = useState("");

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
      setBatch(null);

      try {
        if (isCreate) {
          setForm({});
        } else if (id) {
          const result = await getBatch(id, controller.signal);

          if (controller.signal.aborted) return;

          setBatch(result);
          setForm({
            batchName: result.batchName,
            discipline: result.discipline,
            level: result.level,
            startTime: result.startTime,
            endTime: result.endTime,
            capacity: String(result.capacity),
            status: result.status,
          });

          if (!isEdit) {
            const [studentRoster, teacherRoster, allStudents, allTeachers] =
              await Promise.all([
                getBatchMembers(id, "students", controller.signal),
                getBatchMembers(id, "teachers", controller.signal),
                getStudents(controller.signal),
                listPeople("teachers", "", controller.signal),
              ]);

            if (controller.signal.aborted) return;

            setStudents(studentRoster);
            setTeachers(teacherRoster);

            setStudentChoices(
              allStudents
                .filter((item) =>
                  !studentRoster.some(
                    (member) => member.memberId === item.studentId,
                  ),
                )
                .map((item) => ({
                  id: String(item.studentId),
                  label:
                    `${item.firstName ?? ""} ${item.lastName ?? ""}`.trim()
                    + ` (${item.admissionNumber || item.username})`,
                })),
            );

            setTeacherChoices(
              allTeachers
                .filter((item) =>
                  !teacherRoster.some(
                    (member) =>
                      String(member.memberId) === String(item.teacherId),
                  ),
                )
                .map((item) => ({
                  id: String(item.teacherId),
                  label:
                    `${item.firstName ?? ""} ${item.lastName ?? ""}`.trim()
                    + ` (${item.employeeCode || item.username})`,
                })),
            );
          }
        } else {
          const result = await getBatches(keyword, controller.signal);

          if (!controller.signal.aborted) {
            setBatches(result);
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
  }, [id, isCreate, isEdit, keyword, attempt]);

  function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setKeyword(input.trim());
    setAttempt((previous) => previous + 1);
  }

  async function handleSave(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (busy) return;

    setError("");
    setNotice("");

    const capacity = Number(form.capacity);

    if (!Number.isInteger(capacity) || capacity <= 0) {
      setError("Capacity must be a positive whole number.");
      return;
    }

    if (!form.startTime || !form.endTime ||
        form.startTime >= form.endTime) {
      setError("Start time must be before end time.");
      return;
    }

    const payload: BatchPayload = {
      batchName: form.batchName.trim(),
      discipline: form.discipline,
      level: form.level,
      startTime: form.startTime,
      endTime: form.endTime,
      capacity,
    };

    if (isCreate) {
      payload.batchCode = form.batchCode.trim();
    } else {
      payload.status = form.status;
    }

    setBusy(true);

    try {
      await saveBatch(payload, isCreate ? undefined : id);

      if (isCreate) {
        setForm({});
        setNotice("Batch created. Return to the list to view it.");
      } else {
        navigate(`${ROOT}/${id}`, { replace: true });
      }
    } catch (exception) {
      setError(errorMessage(exception));
    } finally {
      setBusy(false);
    }
  }

  async function handleDeactivate() {
    if (!id || busy) return;

    if (!window.confirm(
      "Deactivate this batch? Existing assignments and fees will remain.",
    )) {
      return;
    }

    setBusy(true);
    setError("");
    setNotice("");

    try {
      await deactivateBatch(id);
      setNotice("Batch deactivated.");
      setAttempt((previous) => previous + 1);
    } catch (exception) {
      setError(errorMessage(exception));
    } finally {
      setBusy(false);
    }
  }

  async function handleAssignment(
    kind: "students" | "teachers",
    memberId: string,
    remove = false,
  ) {
    if (!id || !memberId || busy) return;

    if (remove && !window.confirm(
      `Remove this ${kind === "students" ? "student" : "teacher"} assignment?`
      + (kind === "students"
        ? " This deletes the enrollment row; existing fee records remain."
        : ""),
    )) {
      return;
    }

    setBusy(true);
    setError("");
    setNotice("");

    try {
      await changeBatchMember(id, kind, memberId, remove);
      setStudentId("");
      setTeacherId("");
      setNotice(remove ? "Assignment removed." : "Assignment saved.");
      setAttempt((previous) => previous + 1);
    } catch (exception) {
      setError(errorMessage(exception));
    } finally {
      setBusy(false);
    }
  }

  function renderField(field: Field) {
    return (
      <div className="student-field" key={field.key}>
        <label htmlFor={`batch-${field.key}`}>{field.label} *</label>

        {field.options ? (
          <select
            id={`batch-${field.key}`}
            value={form[field.key] ?? ""}
            required
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
            id={`batch-${field.key}`}
            type={field.type ?? "text"}
            min={field.type === "number" ? 1 : undefined}
            step={
              field.type === "number"
                ? 1
                : field.type === "time" ? 1 : undefined
            }
            required
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

  function renderRoster(kind: "students" | "teachers") {
    const roster = kind === "students" ? students : teachers;
    const choices = kind === "students" ? studentChoices : teacherChoices;
    const selected = kind === "students" ? studentId : teacherId;
    const setSelected = kind === "students" ? setStudentId : setTeacherId;

    return (
      <section className="batch-roster" aria-labelledby={`${kind}-heading`}>
        <h2 id={`${kind}-heading`}>
          Assigned {kind} · {roster.length}
        </h2>

        {batch?.status === "ACTIVE" && (
          <form
            className="student-search"
            onSubmit={(event) => {
              event.preventDefault();
              void handleAssignment(kind, selected);
            }}
          >
            <label className="student-field">
              Select {kind === "students" ? "student" : "teacher"}
              <select
                value={selected}
                required
                disabled={busy}
                onChange={(event) => setSelected(event.target.value)}
              >
                <option value="">Select an active profile</option>
                {choices.map((choice) => (
                  <option key={choice.id} value={choice.id}>
                    {choice.label}
                  </option>
                ))}
              </select>
            </label>

            <button
              className="student-button"
              type="submit"
              disabled={busy || !selected}
            >
              Assign
            </button>
          </form>
        )}

        <div className="student-table-wrapper">
          <table className="student-table">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Profile status</th>
                <th scope="col">Assigned on</th>
                {kind === "students" && (
                  <th scope="col">Enrollment</th>
                )}
                <th scope="col">Action</th>
              </tr>
            </thead>

            <tbody>
              {roster.map((member) => (
                <tr key={member.memberId}>
                  <td>
                    {`${member.firstName ?? ""} ${member.lastName ?? ""}`
                      .trim() || member.username}
                  </td>
                  <td>{member.profileStatus}</td>
                  <td>{member.assignmentDate || "—"}</td>
                  {kind === "students" && (
                    <td>{member.enrollmentStatus || "—"}</td>
                  )}
                  <td>
                    <button
                      className="student-button student-secondary"
                      type="button"
                      disabled={busy}
                      onClick={() =>
                        void handleAssignment(
                          kind,
                          String(member.memberId),
                          true,
                        )
                      }
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              ))}

              {roster.length === 0 && (
                <tr>
                  <td colSpan={kind === "students" ? 5 : 4}>
                    No assignments yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    );
  }

  return (
    <section className="student-module">
      <div className="student-heading">
        <div>
          <p className="admin-eyebrow">Academy management</p>
          <h1>
            {isCreate
              ? "Add batch"
              : isEdit ? "Edit batch" : isList ? "Batches" : "Batch details"}
          </h1>
        </div>

        <Link className="student-button" to={isList ? `${ROOT}/new` : ROOT}>
          {isList ? "+ Add batch" : "Back to batches"}
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
              Search batches
              <input
                type="search"
                value={input}
                placeholder="Batch name or code"
                onChange={(event) => setInput(event.target.value)}
              />
            </label>

            <button className="student-button" type="submit">Search</button>

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
                <caption>Active batches · {batches.length} records</caption>
                <thead>
                  <tr>
                    <th scope="col">Code</th>
                    <th scope="col">Name</th>
                    <th scope="col">Level</th>
                    <th scope="col">Time</th>
                    <th scope="col">Capacity</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {batches.map((item) => (
                    <tr key={item.batchId}>
                      <td>{item.batchCode}</td>
                      <td>{item.batchName}</td>
                      <td>{item.level}</td>
                      <td>{item.startTime}–{item.endTime}</td>
                      <td>{item.capacity}</td>
                      <td>
                        <div className="student-actions">
                          <Link to={`${ROOT}/${item.batchId}`}>View</Link>
                          <Link to={`${ROOT}/${item.batchId}/edit`}>Edit</Link>
                        </div>
                      </td>
                    </tr>
                  ))}

                  {batches.length === 0 && (
                    <tr><td colSpan={6}>No active batches found.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </>
      ) : isCreate || isEdit ? (
        (isCreate || batch) && (
          <form className="student-form" onSubmit={handleSave}>
            <fieldset disabled={busy}>
              <legend>Batch information</legend>

              <div className="student-form-grid">
                {isCreate && renderField({
                  key: "batchCode",
                  label: "Batch code",
                })}

                {fields.map(renderField)}

                {isEdit && renderField({
                  key: "status",
                  label: "Status",
                  options: ["ACTIVE", "INACTIVE"],
                })}
              </div>
            </fieldset>

            <div className="student-actions">
              <button className="student-button" type="submit" disabled={busy}>
                {busy ? "Saving…" : isCreate ? "Create batch" : "Save changes"}
              </button>
              <Link to={id ? `${ROOT}/${id}` : ROOT}>Cancel</Link>
            </div>
          </form>
        )
      ) : batch ? (
        <>
          <div className="student-details">
            <dl className="student-details-grid">
              {[
                ["Code", batch.batchCode],
                ["Name", batch.batchName],
                ["Discipline", batch.discipline],
                ["Level", batch.level],
                ["Start time", batch.startTime],
                ["End time", batch.endTime],
                ["Capacity", batch.capacity],
                ["Status", batch.status],
              ].map(([label, value]) => (
                <div key={String(label)}>
                  <dt>{label}</dt>
                  <dd>{value}</dd>
                </div>
              ))}
            </dl>

            <div className="student-actions">
              <Link className="student-button" to={`${ROOT}/${id}/edit`}>
                Edit batch
              </Link>

              {batch.status === "ACTIVE" && (
                <button
                  className="student-button student-danger"
                  type="button"
                  disabled={busy}
                  onClick={() => void handleDeactivate()}
                >
                  Deactivate batch
                </button>
              )}
            </div>
          </div>

          {renderRoster("students")}
          {renderRoster("teachers")}
        </>
      ) : null}
    </section>
  );
}