import { useEffect, useState } from "react";
import type { FormEvent } from "react";

import {
  generateIndividualFee,
  generateMonthlyFees,
  getFees,
  markOverdueFees,
} from "../api/fees";
import type { Fee } from "../api/fees";

import { getBatches, getBatchMembers } from "../api/batches";
import type { Batch, BatchMember } from "../api/batches";

import "../css/StudentManagement.css";
import "../css/FeeManagement.css";

function messageFrom(exception: unknown) {
  return exception instanceof Error
    ? exception.message
    : "Unable to complete the request.";
}

function formatMoney(value: number | string) {
  const amount = Number(value);

  return Number.isFinite(amount)
    ? new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
      }).format(amount)
    : "—";
}

export default function FeeManagement() {
  const [fees, setFees] = useState<Fee[]>([]);
  const [batches, setBatches] = useState<Batch[]>([]);
  const [members, setMembers] = useState<BatchMember[]>([]);

  const [batchId, setBatchId] = useState("");
  const [studentId, setStudentId] = useState("");
  const [billingMonth, setBillingMonth] = useState("");

  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [monthFilter, setMonthFilter] = useState("");

  const [loading, setLoading] = useState(true);
  const [membersLoading, setMembersLoading] = useState(false);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [membersError, setMembersError] = useState("");
  const [notice, setNotice] = useState("");
  const [attempt, setAttempt] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    async function load() {
      setLoading(true);
      setError("");

      try {
        const [feeResult, batchResult] = await Promise.all([
          getFees(controller.signal),
          getBatches("", controller.signal),
        ]);

        if (!controller.signal.aborted) {
          setFees(feeResult);
          setBatches(batchResult);
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
  }, [attempt]);

  useEffect(() => {
    const controller = new AbortController();

    async function loadMembers() {
      setMembers([]);
      setStudentId("");
      setMembersError("");
      setMembersLoading(Boolean(batchId));

      if (!batchId) return;

      try {
        const result = await getBatchMembers(
          batchId,
          "students",
          controller.signal,
        );

        if (!controller.signal.aborted) {
          setMembers(
            result.filter(
              (member) => member.enrollmentStatus === "ACTIVE",
            ),
          );
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setMembersError(messageFrom(exception));
        }
      } finally {
        if (!controller.signal.aborted) {
          setMembersLoading(false);
        }
      }
    }

    void loadMembers();
    return () => controller.abort();
  }, [batchId, attempt]);

  async function runAction(action: () => Promise<string>) {
    if (busy || loading) return;

    setBusy(true);
    setError("");
    setNotice("");

    try {
      const message = await action();
      setNotice(message);
      setAttempt((previous) => previous + 1);
    } catch (exception) {
      setError(messageFrom(exception));
    } finally {
      setBusy(false);
    }
  }

  function handleIndividual(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!batchId || !studentId || !billingMonth) return;

    void runAction(async () => {
      const created = await generateIndividualFee(
        Number(studentId),
        Number(batchId),
        billingMonth,
      );

      return created
        ? "Fee generated successfully."
        : "No fee created: it already exists or the enrollment is not eligible for this month.";
    });
  }

  function handleMonthly() {
    if (!billingMonth) return;

    if (!window.confirm(
      `Generate fees for all eligible enrollments for ${billingMonth}? ` +
      "This is not limited to the selected batch.",
    )) {
      return;
    }

    void runAction(async () => {
      const count = await generateMonthlyFees(billingMonth);
      return `${count} fee records generated.`;
    });
  }

  function handleOverdue() {
    if (!window.confirm(
      "Mark pending fees whose due date has passed as overdue?",
    )) {
      return;
    }

    void runAction(async () => {
      const count = await markOverdueFees();
      return `${count} fee records marked overdue.`;
    });
  }

  const normalizedSearch = search.trim().toLowerCase();

  const visibleFees = fees.filter((fee) => {
    const searchable = [
      fee.studentName,
      fee.batchName,
      String(fee.feeId),
      String(fee.studentId),
    ].join(" ").toLowerCase();

    return (
      (!normalizedSearch || searchable.includes(normalizedSearch)) &&
      (!statusFilter || fee.feeStatus === statusFilter) &&
      (!monthFilter || fee.billingMonth?.startsWith(monthFilter))
    );
  });

  return (
    <section className="student-module">
      <div className="student-heading">
        <div>
          <p className="admin-eyebrow">Academy management</p>
          <h1>Fees</h1>
        </div>

        <button
          className="student-button student-secondary"
          type="button"
          disabled={busy || loading}
          onClick={() => setAttempt((previous) => previous + 1)}
        >
          Refresh
        </button>
      </div>

      <p className="student-hint">
        Fee amounts are determined by the configured batch-level rate.
        Payment collection is handled separately.
      </p>

      {notice && (
        <p className="student-notice" role="status">{notice}</p>
      )}

      {error && (
        <p className="student-error" role="alert">{error}</p>
      )}

      <form className="student-form" onSubmit={handleIndividual}>
        <fieldset disabled={busy || loading}>
          <legend>Generate fees</legend>

          <div className="student-form-grid">
            <label className="student-field">
              Billing month *
              <input
                type="month"
                required
                value={billingMonth}
                onChange={(event) => setBillingMonth(event.target.value)}
              />
            </label>

            <label className="student-field">
              Batch *
              <select
                required
                value={batchId}
                onChange={(event) => {
                  setBatchId(event.target.value);
                  setStudentId("");
                  setMembers([]);
                }}
              >
                <option value="">Select a batch</option>
                {batches.map((batch) => (
                  <option key={batch.batchId} value={batch.batchId}>
                    {batch.batchName} ({batch.batchCode})
                  </option>
                ))}
              </select>
            </label>

            <label className="student-field">
              Enrolled student *
              <select
                required
                disabled={!batchId || membersLoading}
                value={studentId}
                onChange={(event) => setStudentId(event.target.value)}
              >
                <option value="">
                  {membersLoading ? "Loading…" : "Select a student"}
                </option>

                {members.map((member) => (
                  <option key={member.memberId} value={member.memberId}>
                    {member.firstName} {member.lastName}
                    {" "}({member.username})
                  </option>
                ))}
              </select>
            </label>
          </div>

          {membersError && (
            <p className="student-error" role="alert">{membersError}</p>
          )}

          {!membersLoading && batchId && !membersError &&
            members.length === 0 && (
              <p className="student-hint">
                No active enrollments found in this batch.
              </p>
            )}

          <div className="student-actions fee-actions">
            <button
              className="student-button"
              type="submit"
              disabled={!studentId || !billingMonth || membersLoading}
            >
              Generate individual fee
            </button>

            <button
              className="student-button student-secondary"
              type="button"
              disabled={!billingMonth}
              onClick={handleMonthly}
            >
              Generate all eligible fees
            </button>
          </div>

          <p className="student-hint">
            The bulk action uses the billing month only, across all eligible
            enrollments. Existing fees are skipped.
          </p>
        </fieldset>
      </form>

      <section className="fee-records" aria-labelledby="fee-records-heading">
        <div className="student-heading">
          <h2 id="fee-records-heading">Fee records</h2>

          <button
            className="student-button student-secondary"
            type="button"
            disabled={busy || loading}
            onClick={handleOverdue}
          >
            Mark overdue
          </button>
        </div>

        <div className="fee-filters">
          <label className="student-field">
            Search loaded records
            <input
              type="search"
              placeholder="Student, batch, or fee ID"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
            />
          </label>

          <label className="student-field">
            Status
            <select
              value={statusFilter}
              onChange={(event) => setStatusFilter(event.target.value)}
            >
              <option value="">All statuses</option>
              <option value="PENDING">Pending</option>
              <option value="PAID">Paid</option>
              <option value="OVERDUE">Overdue</option>
            </select>
          </label>

          <label className="student-field">
            Billing month
            <input
              type="month"
              value={monthFilter}
              onChange={(event) => setMonthFilter(event.target.value)}
            />
          </label>

          <button
            className="student-button student-secondary"
            type="button"
            onClick={() => {
              setSearch("");
              setStatusFilter("");
              setMonthFilter("");
            }}
          >
            Clear filters
          </button>
        </div>

        {loading ? (
          <p role="status">Loading fees…</p>
        ) : (
          <div className="student-table-wrapper">
            <table className="student-table">
              <caption>{visibleFees.length} matching fee records</caption>

              <thead>
                <tr>
                  <th scope="col">Fee ID</th>
                  <th scope="col">Student</th>
                  <th scope="col">Batch</th>
                  <th scope="col">Month</th>
                  <th scope="col">Due date</th>
                  <th scope="col">Amount</th>
                  <th scope="col">Status</th>
                  <th scope="col">Remarks</th>
                </tr>
              </thead>

              <tbody>
                {visibleFees.map((fee) => (
                  <tr key={fee.feeId}>
                    <td>{fee.feeId}</td>
                    <td>{fee.studentName}</td>
                    <td>{fee.batchName}</td>
                    <td>{fee.billingMonth?.slice(0, 7)}</td>
                    <td>{fee.dueDate}</td>
                    <td>{formatMoney(fee.feeAmount)}</td>
                    <td>{fee.feeStatus}</td>
                    <td>{fee.remarks || "—"}</td>
                  </tr>
                ))}

                {visibleFees.length === 0 && (
                  <tr>
                    <td colSpan={8}>
                      {error
                        ? "Records could not be refreshed. Please retry."
                        : "No matching fee records."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </section>
  );
}