import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  Link,
  useLocation,
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  cancelEvent,
  getEvent,
  getEvents,
  saveEvent,
} from "../api/events";

import type { AcademyEvent, EventPayload } from "../api/events";
import EventParticipants from "../components/EventParticipations";

import "../css/StudentManagement.css";
import "../css/EventManagement.css";

const ROOT = "/dashboard/admin/events";

const eventTypes = [
  "ANNUAL_DAY",
  "ARANGETRAM",
  "COMPETITION",
  "WORKSHOP",
  "PERFORMANCE",
  "EXAM_EVENT",
];

const eventStatuses = [
  "PLANNED",
  "ONGOING",
  "COMPLETED",
  "CANCELLED",
];

type Field = {
  key: string;
  label: string;
  type?: string;
  options?: string[];
};

const fields: Field[] = [
  { key: "eventName", label: "Event name" },
  { key: "eventType", label: "Event type", options: eventTypes },
  { key: "eventDate", label: "Event date", type: "date" },
  { key: "venue", label: "Venue" },
];

function messageFrom(exception: unknown) {
  return exception instanceof Error
    ? exception.message
    : "Unable to complete the request.";
}

export default function EventManagement() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const isCreate = location.pathname.endsWith("/new");
  const isEdit = Boolean(id) && location.pathname.endsWith("/edit");
  const isList = !id && !isCreate;

  const [events, setEvents] = useState<AcademyEvent[]>([]);
  const [event, setEvent] = useState<AcademyEvent | null>(null);
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
      setEvent(null);

      try {
        if (isCreate) {
          setForm({});
        } else if (id) {
          const result = await getEvent(id, controller.signal);

          if (!controller.signal.aborted) {
            setEvent(result);
            setForm({
              eventName: result.eventName ?? "",
              eventType: result.eventType ?? "",
              eventDate: result.eventDate ?? "",
              venue: result.venue ?? "",
              description: result.description ?? "",
              status: result.status ?? "",
            });
          }
        } else {
          const result = await getEvents(keyword, controller.signal);

          if (!controller.signal.aborted) {
            setEvents(result);
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

  function handleSearch(submission: FormEvent<HTMLFormElement>) {
    submission.preventDefault();
    setKeyword(input.trim());
    setAttempt((previous) => previous + 1);
  }

  async function handleSave(submission: FormEvent<HTMLFormElement>) {
    submission.preventDefault();
    if (busy) return;

    setError("");
    setNotice("");

    if (
      !form.eventName?.trim() ||
      !form.eventType ||
      !form.eventDate ||
      !form.venue?.trim() ||
      (isCreate && !form.eventCode?.trim()) ||
      (isEdit && !form.status)
    ) {
      setError("Complete all required fields.");
      return;
    }

    const payload: EventPayload = {
      eventName: form.eventName.trim(),
      eventType: form.eventType,
      eventDate: form.eventDate,
      venue: form.venue.trim(),
      description: form.description?.trim() || null,
    };

    if (isCreate) {
      payload.eventCode = form.eventCode.trim();
    } else {
      payload.status = form.status;
    }

    setBusy(true);

    try {
      await saveEvent(payload, isCreate ? undefined : id);

      if (isCreate) {
        setForm({});
        setNotice("Event created. Return to the list to view it.");
      } else {
        navigate(`${ROOT}/${id}`, { replace: true });
      }
    } catch (exception) {
      setError(messageFrom(exception));
    } finally {
      setBusy(false);
    }
  }

  async function handleCancel() {
    if (!id || busy) return;

    if (!window.confirm(
      "Cancel this event? The event and its registrations will remain.",
    )) {
      return;
    }

    setBusy(true);
    setError("");
    setNotice("");

    try {
      await cancelEvent(id);

      setEvent((previous) =>
        previous ? { ...previous, status: "CANCELLED" } : previous,
      );

      setNotice("Event cancelled.");
    } catch (exception) {
      setError(messageFrom(exception));
    } finally {
      setBusy(false);
    }
  }

  function renderField(field: Field) {
    return (
      <div className="student-field" key={field.key}>
        <label htmlFor={`event-${field.key}`}>{field.label} *</label>

        {field.options ? (
          <select
            id={`event-${field.key}`}
            required
            value={form[field.key] ?? ""}
            onChange={(change) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: change.target.value,
              }))
            }
          >
            <option value="">Select</option>
            {field.options.map((option) => (
              <option key={option} value={option}>
                {option.replaceAll("_", " ")}
              </option>
            ))}
          </select>
        ) : (
          <input
            id={`event-${field.key}`}
            type={field.type ?? "text"}
            required
            value={form[field.key] ?? ""}
            onChange={(change) =>
              setForm((previous) => ({
                ...previous,
                [field.key]: change.target.value,
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
            {isCreate
              ? "Add event"
              : isEdit
                ? "Edit event"
                : isList
                  ? "Events"
                  : "Event details"}
          </h1>
        </div>

        <Link className="student-button" to={isList ? `${ROOT}/new` : ROOT}>
          {isList ? "+ Add event" : "Back to events"}
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
              disabled={busy}
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
              Search events
              <input
                type="search"
                placeholder="Event name or venue"
                value={input}
                onChange={(change) => setInput(change.target.value)}
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
                <caption>Events · {events.length} records</caption>

                <thead>
                  <tr>
                    <th scope="col">Code</th>
                    <th scope="col">Event</th>
                    <th scope="col">Date</th>
                    <th scope="col">Venue</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {events.map((item) => (
                    <tr key={item.eventId}>
                      <td>{item.eventCode}</td>
                      <td>{item.eventName}</td>
                      <td>{item.eventDate}</td>
                      <td>{item.venue}</td>
                      <td>{item.status}</td>
                      <td>
                        <div className="student-actions">
                          <Link to={`${ROOT}/${item.eventId}`}>View</Link>
                          <Link to={`${ROOT}/${item.eventId}/edit`}>
                            Edit
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}

                  {events.length === 0 && (
                    <tr><td colSpan={6}>No events found.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </>
      ) : isCreate || isEdit ? (
        (isCreate || event) && (
          <form className="student-form" onSubmit={handleSave}>
            <fieldset disabled={busy}>
              <legend>Event information</legend>

              <div className="student-form-grid">
                {isCreate && renderField({
                  key: "eventCode",
                  label: "Event code",
                })}

                {fields.map(renderField)}

                {isEdit && renderField({
                  key: "status",
                  label: "Status",
                  options: eventStatuses,
                })}

                <div className="student-field event-description-field">
                  <label htmlFor="event-description">Description</label>
                  <textarea
                    id="event-description"
                    rows={5}
                    value={form.description ?? ""}
                    onChange={(change) =>
                      setForm((previous) => ({
                        ...previous,
                        description: change.target.value,
                      }))
                    }
                  />
                </div>
              </div>
            </fieldset>

            <div className="student-actions">
              <button
                className="student-button"
                type="submit"
                disabled={busy}
              >
                {busy
                  ? "Saving…"
                  : isCreate ? "Create event" : "Save changes"}
              </button>

              <Link to={id ? `${ROOT}/${id}` : ROOT}>
                Cancel editing
              </Link>
            </div>
          </form>
        )
      ) : event ? (
        <>
          <div className="student-details">
            <dl className="student-details-grid">
              {[
                ["Code", event.eventCode],
                ["Name", event.eventName],
                ["Type", event.eventType?.replaceAll("_", " ")],
                ["Date", event.eventDate],
                ["Venue", event.venue],
                ["Status", event.status],
              ].map(([label, value]) => (
                <div key={label}>
                  <dt>{label}</dt>
                  <dd>{value || "—"}</dd>
                </div>
              ))}

              <div className="event-description-field">
                <dt>Description</dt>
                <dd className="event-description">
                  {event.description || "No description provided."}
                </dd>
              </div>
            </dl>

            <div className="student-actions">
              <Link className="student-button" to={`${ROOT}/${id}/edit`}>
                Edit event
              </Link>

              {event.status !== "CANCELLED" && (
                <button
                  className="student-button student-danger"
                  type="button"
                  disabled={busy}
                  onClick={() => void handleCancel()}
                >
                  {busy ? "Cancelling…" : "Cancel event"}
                </button>
              )}
            </div>
          </div>

          <EventParticipants
            key={`${event.eventId}-${event.status}`}
            eventId={String(event.eventId)}
            cancelled={busy || event.status === "CANCELLED"}
          />
        </>
      ) : null}
    </section>
  );
}