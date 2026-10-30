import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { useAuth } from "../auth/AuthContext";
import { logout } from "../api/auth";
import { getDashboardRecords } from "../api/dashboard";
import { payFee } from "../api/checkout";
import ProfileOverview from "../components/ProfileOverview";

import type {
  DashboardRecord,
  DashboardSection,
} from "../api/dashboard";

import logo from "../assets/logo.jpeg";
import "../css/RoleDashboard.css";

type MenuItem = {
  key: DashboardSection;
  label: string;
};

type Column = {
  key: string;
  label: string;
};

const roleNames: Record<string, string> = {
  STUDENT: "Student",
  TEACHER: "Teacher",
  PARENT: "Parent",
};

function menuFor(role: string): MenuItem[] {
  const items: MenuItem[] = [
    { key: "overview", label: "Overview" },
    { key: "events", label: "Academy events" },
  ];

  if (role === "STUDENT") {
    items.push(
      { key: "batches", label: "My batches" },
      { key: "fees", label: "My fees" },
    );
  } else if (role === "TEACHER") {
    items.push(
      { key: "students", label: "Academy students" },
      { key: "batches", label: "Academy batches" },
    );
  } else if (role === "PARENT") {
    items.push({ key: "fees", label: "Children’s fees" });
  }

  return items;
}

function columnsFor(section: DashboardSection): Column[] {
  switch (section) {
    case "events":
      return [
        { key: "eventName", label: "Event" },
        { key: "eventDate", label: "Date" },
        { key: "venue", label: "Venue" },
        { key: "status", label: "Status" },
      ];

    case "fees":
      return [
        { key: "feeId", label: "Fee ID" },
        { key: "studentName", label: "Student" },
        { key: "batchName", label: "Batch" },
        { key: "billingMonth", label: "Billing month" },
        { key: "dueDate", label: "Due date" },
        { key: "feeAmount", label: "Amount" },
        { key: "feeStatus", label: "Status" },
      ];

    case "students":
      return [
        { key: "admissionNumber", label: "Admission number" },
        { key: "firstName", label: "First name" },
        { key: "lastName", label: "Last name" },
        { key: "status", label: "Profile status" },
      ];

    case "batches":
      return [
        { key: "batchCode", label: "Code" },
        { key: "batchName", label: "Batch" },
        { key: "discipline", label: "Discipline" },
        { key: "level", label: "Level" },
        { key: "startTime", label: "Start" },
        { key: "endTime", label: "End" },
      ];

    default:
      return [];
  }
}

function formatValue(key: string, value: unknown): string {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  if (key === "feeAmount") {
    const amount = Number(value);

    return Number.isFinite(amount)
      ? new Intl.NumberFormat("en-IN", {
          style: "currency",
          currency: "INR",
        }).format(amount)
      : "—";
  }

  if (key === "billingMonth") {
    return String(value).slice(0, 7);
  }

  return String(value).replaceAll("_", " ");
}

export default function DashboardPage() {
  const { user, refreshUser } = useAuth();
  const navigate = useNavigate();

  const role = user?.role ?? "";
  const menu = menuFor(role);

  const [section, setSection] =
    useState<DashboardSection>("overview");
  const [records, setRecords] = useState<DashboardRecord[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [attempt, setAttempt] = useState(0);

  const [loggingOut, setLoggingOut] = useState(false);
  const [logoutError, setLogoutError] = useState("");

  const [payingFeeId, setPayingFeeId] = useState<number | null>(null);
  const [paymentMessage, setPaymentMessage] = useState("");
  const [paymentError, setPaymentError] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    async function load() {
      setRecords([]);
      setError("");

      if (!role || section === "overview") {
        setLoading(false);
        return;
      }

      setLoading(true);

      try {
        const result = await getDashboardRecords(
          role,
          section,
          controller.signal,
        );

        if (!controller.signal.aborted) {
          setRecords(result);
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(
            exception instanceof Error
              ? exception.message
              : "Unable to load this section.",
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void load();

    return () => controller.abort();
  }, [role, section, attempt]);

  function changeSection(nextSection: DashboardSection) {
    if (payingFeeId !== null || nextSection === section) return;

    setRecords([]);
    setError("");
    setPaymentMessage("");
    setPaymentError("");
    setSection(nextSection);
  }

  async function handleLogout() {
    if (loggingOut || payingFeeId !== null) return;

    setLoggingOut(true);
    setLogoutError("");

    try {
      await logout();

      try {
        await refreshUser();
      } catch {
        // Logout succeeded even if refreshing the user fails.
      }

      navigate("/login", { replace: true });
    } catch (exception) {
      setLogoutError(
        exception instanceof Error
          ? exception.message
          : "Unable to log out.",
      );
    } finally {
      setLoggingOut(false);
    }
  }

  async function handlePay(feeId: number) {
    if (payingFeeId !== null || loading || loggingOut) return;

    if (!Number.isSafeInteger(feeId) || feeId <= 0) {
      setPaymentError("This fee has an invalid ID. Please refresh.");
      return;
    }

    setPayingFeeId(feeId);
    setPaymentMessage("");
    setPaymentError("");

    try {
      const result = await payFee(feeId);

      setPaymentMessage(
        result
          ? "Payment verified successfully. Your fee is now paid."
          : "Checkout closed. Payment has not been confirmed.",
      );
    } catch (exception) {
      setPaymentError(
        exception instanceof Error
          ? exception.message
          : "Unable to complete payment.",
      );
    } finally {
      setPayingFeeId(null);
      setAttempt((previous) => previous + 1);
    }
  }

  if (!user) return null;

  const title =
    menu.find((item) => item.key === section)?.label ?? "Overview";

  const columns = columnsFor(section);

  const showPaymentActions =
    section === "fees" &&
    (role === "STUDENT" || role === "PARENT");

  const columnCount =
    columns.length + (showPaymentActions ? 1 : 0);

  return (
    <div className="role-layout">
      <aside className="role-sidebar">
        <Link className="role-brand" to="/">
          <img src={logo} alt="" />
          <span>
            Lasyankali
            <small>NrityaSutra</small>
          </span>
        </Link>

        <p className="role-eyebrow">
          {roleNames[role]} workspace
        </p>

        <nav aria-label={`${roleNames[role]} navigation`}>
          {menu.map((item) => (
            <button
              key={item.key}
              className={
                section === item.key ? "role-nav-active" : ""
              }
              type="button"
              disabled={payingFeeId !== null}
              aria-current={
                section === item.key ? "page" : undefined
              }
              onClick={() => changeSection(item.key)}
            >
              {item.label}
            </button>
          ))}
        </nav>
      </aside>

      <div className="role-workspace">
        <header className="role-header">
          <span>{roleNames[role]} dashboard</span>

          <div className="role-account">
            <span>{user.username}</span>

            <button
              type="button"
              disabled={loggingOut || payingFeeId !== null}
              onClick={() => void handleLogout()}
            >
              {loggingOut ? "Logging out…" : "Logout"}
            </button>
          </div>
        </header>

        <main className="role-main">
          {logoutError && (
            <p className="role-error" role="alert">
              {logoutError}
            </p>
          )}

          <h1>{title}</h1>

          {section === "fees" && paymentMessage && (
            <p className="role-payment-notice" role="status">
              {paymentMessage}
            </p>
          )}

          {section === "fees" && paymentError && (
            <p className="role-error" role="alert">
              {paymentError}
            </p>
          )}

          {section === "overview" ? (
            <>
              <p className="role-introduction">
                Your personal details and academy information,
                together.
              </p>

              <ProfileOverview />

              <h2 className="role-section-title">
                Your workspace
              </h2>

              <div className="role-shortcuts">
                {menu
                  .filter((item) => item.key !== "overview")
                  .map((item) => (
                    <button
                      type="button"
                      key={item.key}
                      onClick={() => changeSection(item.key)}
                    >
                      <span>{item.label}</span>
                      <span aria-hidden="true">→</span>
                    </button>
                  ))}
              </div>
            </>
          ) : (
            <>
              <button
                className="role-refresh"
                type="button"
                disabled={loading || payingFeeId !== null}
                onClick={() =>
                  setAttempt((previous) => previous + 1)
                }
              >
                Refresh
              </button>

              {error ? (
                <div className="role-error" role="alert">
                  <p>{error}</p>
                  <Link to="/login">Go to login</Link>
                </div>
              ) : loading ? (
                <p role="status">Loading…</p>
              ) : (
                <div className="role-table-wrapper">
                  <table className="role-table">
                    <caption>{records.length} records</caption>

                    <thead>
                      <tr>
                        {columns.map((column) => (
                          <th scope="col" key={column.key}>
                            {column.label}
                          </th>
                        ))}

                        {showPaymentActions && (
                          <th scope="col">Payment</th>
                        )}
                      </tr>
                    </thead>

                    <tbody>
                      {records.map((record) => {
                        const recordId =
                          record.eventId ??
                          record.feeId ??
                          record.studentId ??
                          record.batchId;

                        const feeId = Number(record.feeId);

                        const validFeeId =
                          Number.isSafeInteger(feeId) &&
                          feeId > 0;

                        const canPay =
                          record.feeStatus === "PENDING" ||
                          record.feeStatus === "OVERDUE";

                        return (
                          <tr key={String(recordId)}>
                            {columns.map((column) => (
                              <td key={column.key}>
                                {formatValue(
                                  column.key,
                                  record[column.key],
                                )}
                              </td>
                            ))}

                            {showPaymentActions && (
                              <td>
                                {record.feeStatus === "PAID" ? (
                                  <span>Paid</span>
                                ) : canPay ? (
                                  <button
                                    className="role-pay-button"
                                    type="button"
                                    disabled={
                                      payingFeeId !== null ||
                                      loggingOut ||
                                      !validFeeId
                                    }
                                    onClick={() =>
                                      void handlePay(feeId)
                                    }
                                    aria-label={`Pay fee ${feeId}`}
                                  >
                                    {payingFeeId === feeId
                                      ? "Processing…"
                                      : "Pay"}
                                  </button>
                                ) : (
                                  "—"
                                )}
                              </td>
                            )}
                          </tr>
                        );
                      })}

                      {records.length === 0 && (
                        <tr>
                          <td colSpan={columnCount}>
                            No records available.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              )}
            </>
          )}
        </main>
      </div>
    </div>
  );
}