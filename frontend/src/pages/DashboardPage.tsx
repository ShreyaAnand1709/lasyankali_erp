import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import "../css/DashboardPage.css";

const dashboardTitles: Record<string, string> = {
  ADMIN: "Admin dashboard",
  TEACHER: "Teacher dashboard",
  STUDENT: "Student dashboard",
  PARENT: "Parent dashboard",
};

export default function DashboardPage() {
  const { user } = useAuth();

  if (!user) return null;

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <Link className="dashboard-brand" to="/">
          Lasyankali
          <span>NrityaSutra</span>
        </Link>

        <div className="dashboard-account">
          <span>{user.username}</span>
          <span className="dashboard-role">{user.role}</span>
        </div>
      </header>

      <main className="dashboard-content">
        <p className="dashboard-eyebrow">Your academy workspace</p>

        <h1>{dashboardTitles[user.role] ?? "Dashboard"}</h1>

        <p className="dashboard-welcome">
          Welcome back, {user.username}.
        </p>

        <section
          className="dashboard-placeholder"
          aria-labelledby="workspace-heading"
        >
          <h2 id="workspace-heading">Your workspace is ready</h2>
          <p>
            You are signed in. Academy features will be added here
            as we build your dashboard.
          </p>
        </section>
      </main>
    </div>
  );
}