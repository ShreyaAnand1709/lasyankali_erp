import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

type ProtectedRouteProps = {
  allowedRole: string;
};

export default function ProtectedRoute({
  allowedRole,
}: ProtectedRouteProps) {
  const { user, loading, error, refreshUser } = useAuth();

  if (loading) {
    return <p role="status">Checking your session…</p>;
  }

  if (error) {
    return (
      <main>
        <p role="alert">{error}</p>
        <button
          type="button"
          onClick={() => void refreshUser().catch(() => {})}
        >
          Retry
        </button>
      </main>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (user.role !== allowedRole) {
    return (
      <main>
        <h1>Access denied</h1>
        <p>Your account cannot access this dashboard.</p>
      </main>
    );
  }

  return <Outlet />;
}