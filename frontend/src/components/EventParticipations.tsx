import { useEffect, useState } from "react";

import {
  getParticipants,
  updateParticipation,
} from "../api/adminOperation";

import type { Participant } from "../api/adminOperation";

import "../css/EventParticipations.css";

const participationStatuses = [
  "REGISTERED",
  "PARTICIPATED",
  "ABSENT",
  "WINNER",
];

type EventParticipantsProps = {
  eventId: string;
  cancelled: boolean;
};

export default function EventParticipants({
  eventId,
  cancelled,
}: EventParticipantsProps) {
  const [participants, setParticipants] = useState<Participant[]>([]);
  const [selectedStatuses, setSelectedStatuses] =
    useState<Record<number, string>>({});

  const [loading, setLoading] = useState(true);
  const [savingStudentId, setSavingStudentId] =
    useState<number | null>(null);

  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [reloadCount, setReloadCount] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    async function loadParticipants() {
      setLoading(true);
      setError("");
      setNotice("");

      try {
        const result = await getParticipants(
          eventId,
          controller.signal,
        );

        if (controller.signal.aborted) return;

        setParticipants(result);

        const initialStatuses: Record<number, string> = {};

        for (const participant of result) {
          initialStatuses[participant.studentId] =
            participant.status ?? "";
        }

        setSelectedStatuses(initialStatuses);
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(
            exception instanceof Error
              ? exception.message
              : "Unable to load event participants.",
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void loadParticipants();

    return () => controller.abort();
  }, [eventId, reloadCount]);

  async function handleSave(participant: Participant) {
    if (cancelled || savingStudentId !== null) return;

    const status = selectedStatuses[participant.studentId];

    if (!status || status === participant.status) return;

    setSavingStudentId(participant.studentId);
    setError("");
    setNotice("");

    try {
      await updateParticipation(
        eventId,
        participant.studentId,
        status,
      );

      setParticipants((previous) =>
        previous.map((item) =>
          item.studentId === participant.studentId
            ? { ...item, status }
            : item,
        ),
      );

      setNotice(
        `Participation updated for ${participant.studentName}.`,
      );
    } catch (exception) {
      setError(
        exception instanceof Error
          ? exception.message
          : "Unable to update participation.",
      );
    } finally {
      setSavingStudentId(null);
    }
  }

  return (
    <section
      className="event-participants"
      aria-labelledby="event-participants-heading"
    >
      <div className="event-participants-header">
        <div>
          <h2 id="event-participants-heading">Participants</h2>
          <p>Review registrations and update participation results.</p>
        </div>

        <button
          className="event-participants-refresh"
          type="button"
          disabled={loading || savingStudentId !== null}
          onClick={() => setReloadCount((previous) => previous + 1)}
        >
          Refresh
        </button>
      </div>

      {cancelled && (
        <p className="event-participants-info">
          This event is cancelled. Participation is read-only.
        </p>
      )}

      {notice && (
        <p className="event-participants-success" role="status">
          {notice}
        </p>
      )}

      {error && (
        <p className="event-participants-error" role="alert">
          {error}
        </p>
      )}

      {loading ? (
        <p role="status">Loading participants…</p>
      ) : (
        <div className="event-participants-table-wrapper">
          <table className="event-participants-table">
            <caption>
              {participants.length} registered students
            </caption>

            <thead>
              <tr>
                <th scope="col">Student</th>
                <th scope="col">Registered on</th>
                <th scope="col">Participation</th>
                <th scope="col">Action</th>
              </tr>
            </thead>

            <tbody>
              {participants.map((participant) => {
                const selected =
                  selectedStatuses[participant.studentId] ?? "";

                const isSaving =
                  savingStudentId === participant.studentId;

                return (
                  <tr key={participant.studentId}>
                    <td>{participant.studentName}</td>
                    <td>{participant.registrationDate}</td>

                    <td>
                      <select
                        aria-label={
                          `Participation for ${participant.studentName}`
                        }
                        value={selected}
                        disabled={
                          cancelled || savingStudentId !== null
                        }
                        onChange={(event) =>
                          setSelectedStatuses((previous) => ({
                            ...previous,
                            [participant.studentId]:
                              event.target.value,
                          }))
                        }
                      >
                        <option value="">Select status</option>

                        {participationStatuses.map((status) => (
                          <option key={status} value={status}>
                            {status}
                          </option>
                        ))}
                      </select>
                    </td>

                    <td>
                      <button
                        className="event-participants-save"
                        type="button"
                        disabled={
                          cancelled ||
                          savingStudentId !== null ||
                          !selected ||
                          selected === participant.status
                        }
                        onClick={() => void handleSave(participant)}
                      >
                        {isSaving ? "Saving…" : "Save"}
                      </button>
                    </td>
                  </tr>
                );
              })}

              {participants.length === 0 && (
                <tr>
                  <td colSpan={4}>
                    {error
                      ? "Participants could not be loaded. Click Refresh to retry."
                      : "No registrations yet."}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}