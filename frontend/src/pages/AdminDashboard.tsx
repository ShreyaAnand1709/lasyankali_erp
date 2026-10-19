import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import logo from "../assets/logo.jpeg";
import "../css/AdminDashboard.css";
import { useState } from "react";
import { logout } from "../api/auth";

export default function AdminDashboard() {
  const { user, refreshUser } = useAuth();
const navigate = useNavigate();

const [loggingOut, setLoggingOut] = useState(false);
const [logoutError, setLogoutError] = useState("");

async function handleLogout() {
  if (loggingOut) return;

  setLoggingOut(true);
  setLogoutError("");

  try {
    await logout();
    await refreshUser();
    navigate("/login", { replace: true });
  } catch (exception) {
    setLogoutError(
      exception instanceof Error
        ? exception.message
        : "Unable to log out. Please retry.",
    );
  } finally {
    setLoggingOut(false);
  }
}

  function navClass({ isActive }: { isActive: boolean }) {
    return `admin-nav-item ${isActive ? "admin-nav-item-active" : ""}`;
  }

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <Link className="admin-brand" to="/">
          <img src={logo} alt="" />
          <span>
            Lasyankali
            <small>NrityaSutra</small>
          </span>
        </Link>

        <p className="admin-nav-label">Academy management</p>

        <nav className="admin-navigation" aria-label="Admin navigation">
          <NavLink to="/dashboard/admin" end className={navClass}>
            Overview
          </NavLink>

          <NavLink to="/dashboard/admin/students" className={navClass}>
            Students
          </NavLink>

          <NavLink to="/dashboard/admin/teachers" className={navClass}>
            Teachers
          </NavLink>

          <NavLink to="/dashboard/admin/parents" className={navClass}>
            Parents
          </NavLink>
          <NavLink to="/dashboard/admin/batches" className={navClass}>
            Batches
          </NavLink>

          <NavLink to="/dashboard/admin/events" className={navClass}>
            Events
          </NavLink>

          <NavLink to="/dashboard/admin/fees" className={navClass}>
            Fees
          </NavLink>
        </nav>

        <p className="admin-sidebar-note">
          Rooted in tradition.
          <br />
          Connected through technology.
        </p>
      </aside>

      <div className="admin-workspace">
        <header className="admin-topbar">
          <span className="admin-topbar-label">Academy workspace</span>
          <div className="admin-user">
            <span>{user?.username}</span>
            <span className="admin-role-badge">ADMIN</span>
            <button
              className="admin-logout"
              type="button"
              onClick={() => void handleLogout()}
              disabled={loggingOut}
            >
            {loggingOut ? "Logging out…" : "Logout"}
            </button>
          </div>
        </header>

        {logoutError && (
        <p className="admin-logout-error" role="alert">
        {logoutError}
        </p>
)}

        <main className="admin-main">
          <Outlet />
        </main>
      </div>
    </div>
  );
}