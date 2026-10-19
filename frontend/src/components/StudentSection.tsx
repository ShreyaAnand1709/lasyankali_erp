import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { getStudents } from "../api/students";
import type { Student } from "../api/students";
import "../css/StudentSection.css";

export default function StudentsSection() {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [attempt, setAttempt] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    async function loadStudents() {
      setLoading(true);
      setError("");

      try {
        const result = await getStudents(controller.signal);

        if (!controller.signal.aborted) {
          setStudents(result);
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(
            exception instanceof Error
              ? exception.message
              : "Unable to load students.",
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void loadStudents();

    return () => controller.abort();
  }, [attempt]);

  return (
    <section className="students-section" aria-labelledby="students-heading">
      <p className="admin-eyebrow">Academy management</p>
      <h1 id="students-heading">Students</h1>
      <p className="admin-introduction">
        View the students enrolled in your academy.
      </p>

      {loading ? (
        <p className="students-message" role="status">
          Loading students…
        </p>
      ) : error ? (
        <div className="students-message">
          <p role="alert">{error}</p>

          <button
            className="students-retry"
            type="button"
            onClick={() => setAttempt((previous) => previous + 1)}
          >
            Retry
          </button>

          <Link className="students-login-link" to="/login">
            Go to login
          </Link>
        </div>
      ) : students.length === 0 ? (
        <p className="students-message">No active students found.</p>
      ) : (
        <div className="students-table-wrapper">
          <table className="students-table">
            <caption>
              Active students · {students.length} records
            </caption>

            <thead>
              <tr>
                <th scope="col">Admission number</th>
                <th scope="col">Student</th>
                <th scope="col">Email</th>
                <th scope="col">Mobile</th>
                <th scope="col">Status</th>
              </tr>
            </thead>

            <tbody>
              {students.map((student) => (
                <tr key={student.studentId}>
                  <td>{student.admissionNumber || "—"}</td>

                  <td>
                    <span className="students-name">
                      {[student.firstName, student.lastName]
                        .filter(Boolean)
                        .join(" ") || student.username}
                    </span>
                    <span className="students-username">
                      {student.username}
                    </span>
                  </td>

                  <td>{student.email || "—"}</td>
                  <td>{student.mobileNumber || "—"}</td>
                  <td>{student.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}