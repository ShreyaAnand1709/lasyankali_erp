import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

import {
  requestPasswordReset,
  resetPassword,
} from "../api/passwordReset";

import "../css/PasswordRecovery.css";

type Props = {
  mode: "forgot" | "reset";
};

export default function PasswordRecoveryPage({ mode }: Props) {
  const navigate = useNavigate();
  const isReset = mode === "reset";

  const [token] = useState(() =>
    isReset
      ? new URLSearchParams(window.location.search).get("token") ?? ""
      : "",
  );

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmation, setConfirmation] = useState("");

  const [busy, setBusy] = useState(false);
  const [success, setSuccess] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (isReset && window.location.search) {
      // Keep the token in component memory and remove it from the URL.
      navigate("/reset-password", { replace: true });
    }
  }, [isReset, navigate]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (busy || success) return;

    setError("");
    setMessage("");

    if (isReset) {
      if (!token) {
        setError("The reset link is missing. Request a new link.");
        return;
      }

      if (
        password.trim().length === 0 ||
        password.length < 12 ||
        password.length > 72 ||
        new TextEncoder().encode(password).length > 72
      ) {
        setError(
          "Use 12–72 characters, with a maximum of 72 UTF-8 bytes.",
        );
        return;
      }

      if (password !== confirmation) {
        setError("The passwords do not match.");
        return;
      }
    }

    setBusy(true);

    try {
      const result = isReset
        ? await resetPassword(token, password)
        : await requestPasswordReset(email.trim());

      setMessage(result);
      setSuccess(true);
      setPassword("");
      setConfirmation("");
    } catch (exception) {
      setError(
        exception instanceof Error
          ? exception.message
          : "Unable to complete your request.",
      );
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="recovery-page">
      <section
        className="recovery-card"
        aria-labelledby="recovery-title"
      >
        <Link className="recovery-brand" to="/">
          Lasyankali
          <span>NrityaSutra</span>
        </Link>

        <p className="recovery-eyebrow">Your academy, connected</p>

        <h1 id="recovery-title">
          {isReset ? "Choose a new password" : "Forgot your password?"}
        </h1>

        <p className="recovery-description">
          {isReset
            ? "Set a new password to regain access to your account."
            : "Enter your registered email to request a password reset link."}
        </p>

        {message && (
          <p className="recovery-success" role="status">
            {message}
          </p>
        )}

        {error && (
          <p className="recovery-error" role="alert">
            {error}
          </p>
        )}

        {isReset && !token && !success ? (
          <p className="recovery-error" role="alert">
            This page requires the link sent to your email.
          </p>
        ) : !success ? (
          <form onSubmit={handleSubmit} aria-busy={busy}>
            <fieldset disabled={busy}>
              {isReset ? (
                <>
                  <label htmlFor="recovery-password">
                    New password
                  </label>

                  <input
                    id="recovery-password"
                    type="password"
                    autoComplete="new-password"
                    minLength={12}
                    maxLength={72}
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    aria-describedby="recovery-password-help"
                    required
                  />

                  <p
                    id="recovery-password-help"
                    className="recovery-help"
                  >
                    Use 12–72 characters. A long, unique passphrase
                    works well.
                  </p>

                  <label htmlFor="recovery-confirmation">
                    Confirm new password
                  </label>

                  <input
                    id="recovery-confirmation"
                    type="password"
                    autoComplete="new-password"
                    minLength={12}
                    maxLength={72}
                    value={confirmation}
                    onChange={(event) =>
                      setConfirmation(event.target.value)
                    }
                    required
                  />
                </>
              ) : (
                <>
                  <label htmlFor="recovery-email">
                    Registered email
                  </label>

                  <input
                    id="recovery-email"
                    type="email"
                    autoComplete="email"
                    maxLength={150}
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    required
                  />
                </>
              )}

              <button type="submit">
                {busy
                  ? "Please wait…"
                  : isReset
                    ? "Reset password"
                    : "Send reset link"}
              </button>
            </fieldset>
          </form>
        ) : null}

        {isReset && !success && (
          <Link className="recovery-link" to="/forgot-password">
            Request a new reset link
          </Link>
        )}

        <Link className="recovery-link" to="/login">
          Back to login
        </Link>
      </section>
    </main>
  );
}