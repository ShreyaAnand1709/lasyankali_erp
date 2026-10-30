import { useState } from "react";
import type { FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

import { login } from "../api/auth";
import { useAuth } from "../auth/AuthContext";
import loginHero from "../assets/loginHero.jpg";
import "../css/LoginPage.css";

const dashboardRoutes: Record<string, string> = {
  ADMIN: "/dashboard/admin",
  TEACHER: "/dashboard/teacher",
  STUDENT: "/dashboard/student",
  PARENT: "/dashboard/parent",
};

export default function LoginPage() {
  const navigate = useNavigate();
  const { refreshUser } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  async function handleLogin(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (busy) return;

    setError("");
    setBusy(true);

    try {
      await login(username.trim(), password);

      const user = await refreshUser();

      if (!user) {
        throw new Error(
          "Your session could not be established. Please try again.",
        );
      }

      const destination = dashboardRoutes[user.role];

      if (!destination) {
        throw new Error(
          "Your account role is not supported. Contact your administrator.",
        );
      }

      setPassword("");
      navigate(destination, { replace: true });
    } catch (exception) {
      setError(
        exception instanceof Error
          ? exception.message
          : "Unable to log in. Please try again.",
      );
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-art" aria-label="Indian classical dance">
        <img
          className="login-art-image"
          src={loginHero}
          alt="Illustration of an Indian classical dancer in burgundy and gold"
        />

        <Link className="login-back" to="/">
          <span aria-hidden="true">←</span> Back to home
        </Link>

        <div className="login-art-caption">
          <p>Art. Tradition. Connection.</p>
          <h2>Rooted in dance.</h2>
        </div>
      </section>

      <section className="login-panel" aria-labelledby="login-heading">
        <div className="login-form-container">
          <Link className="login-brand" to="/">
            Lasyankali
            <span>NrityaSutra</span>
          </Link>

          <div className="login-introduction">
            <p className="login-eyebrow">Your academy, connected</p>
            <h1 id="login-heading">Welcome back</h1>
            <p>Login with the credentials provided by your administrator.</p>
          </div>

          <form
            className="login-form"
            onSubmit={handleLogin}
            aria-busy={busy}
          >
            <div className="login-field">
              <label htmlFor="login-username">Username</label>
              <input
                id="login-username"
                name="username"
                type="text"
                autoComplete="username"
                placeholder="Enter your username"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                required
                disabled={busy}
              />
            </div>

            <div className="login-field">
              <label htmlFor="login-password">Password</label>
              
              <div className="login-password-wrapper">
                <input
                  id="login-password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  autoComplete="current-password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  required
                  disabled={busy}
                />

                <button
                  className="login-password-toggle"
                  type="button"
                  onClick={() => setShowPassword((previous) => !previous)}
                  aria-label={
                    showPassword ? "Hide password" : "Show password"
                  }
                  aria-controls="login-password"
                  disabled={busy}
                >
                  {showPassword ? "Hide" : "Show"}
                </button>
              </div>
              <Link className="recovery-link" to="/forgot-password">
                Forgot password?
              </Link>
            </div>

            {error && (
              <p className="login-error" role="alert">
                {error}
              </p>
            )}

            <button
              className="login-submit"
              type="submit"
              disabled={busy || !username.trim() || !password}
            >
              {busy ? "Logging in…" : "Login"}
            </button>
          </form>

          <p className="login-help">
            Having trouble accessing your account? Contact your academy
            administrator.
          </p>

          <footer className="login-footer">
            © {new Date().getFullYear()} Lasyankali: NrityaSutra
          </footer>
        </div>
      </section>
    </main>
  );
}