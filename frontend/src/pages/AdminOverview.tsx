import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function AdminOverview() {
  const { user } = useAuth();

  return (
    <>
      <p className="admin-eyebrow">Overview</p>
      <h1>Welcome back, {user?.username}.</h1>

      <p className="admin-introduction">
        Manage your academy’s students, people, and activities.
      </p>

      <div className="admin-section-heading">
        <h2>Academy management</h2>
      </div>

      <div className="admin-module-grid">
        {["Students", "Teachers", "Parents", "Batches", "Events", "Fees"].map(
          (name, index) => (
            <article className="admin-module-card" key={name}>
              <span className="admin-module-number" aria-hidden="true">
                {String(index + 1).padStart(2, "0")}
              </span>
              <h3>{name}</h3>

              {["Students", "Teachers", "Parents"].includes(name) ? (
                <Link to={`/dashboard/admin/${name.toLowerCase()}`}>
                Manage {name.toLowerCase()} →
                </Link>
              ) : (
                <span className="admin-module-status">Coming soon</span>
              )}
            </article>
          ),
        )}
      </div>
    </>
  );
}