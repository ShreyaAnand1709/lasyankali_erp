import {
  BrowserRouter,
  Link,
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import AdminDashboard from "./pages/AdminDashboard";
import AdminOverview from "./pages/AdminOverview";
import DashboardPage from "./pages/DashboardPage";
import StudentManagement from "./pages/StudentManagement";
import PeopleManagement from "./pages/PeopleManagement";
import BatchManagement from "./pages/BatchManagement";
import ProtectedRoute from "./components/ProtectedRoute";
import EventManagement from "./pages/EventManagement";
import FeeManagement from "./pages/FeeManagement";
import PasswordRecoveryPage from "./pages/PasswordRecoveryPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public pages */}
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
  path="/forgot-password"
  element={
    <PasswordRecoveryPage key="forgot-password" mode="forgot" />
  }
/>

<Route
  path="/reset-password"
  element={
    <PasswordRecoveryPage key="reset-password" mode="reset" />
  }
/>

        {/* Admin workspace */}
        <Route element={<ProtectedRoute allowedRole="ADMIN" />}>
          <Route path="/dashboard/admin" element={<AdminDashboard />}>
            <Route index element={<AdminOverview />} />

            {/* Students */}
            <Route
              path="students"
              element={<StudentManagement key="students-list" />}
            />
            <Route
              path="students/new"
              element={<StudentManagement key="students-new" />}
            />
            <Route
              path="students/:id"
              element={<StudentManagement key="students-view" />}
            />
            <Route
              path="students/:id/edit"
              element={<StudentManagement key="students-edit" />}
            />

            {/* Teachers */}
            <Route
              path="teachers"
              element={
                <PeopleManagement key="teachers-list" kind="teachers" />
              }
            />
            <Route
              path="teachers/new"
              element={
                <PeopleManagement key="teachers-new" kind="teachers" />
              }
            />
            <Route
              path="teachers/:id"
              element={
                <PeopleManagement key="teachers-view" kind="teachers" />
              }
            />
            <Route
              path="teachers/:id/edit"
              element={
                <PeopleManagement key="teachers-edit" kind="teachers" />
              }
            />

            {/* Parents */}
            <Route
              path="parents"
              element={
                <PeopleManagement key="parents-list" kind="parents" />
              }
            />
            <Route
              path="parents/new"
              element={
                <PeopleManagement key="parents-new" kind="parents" />
              }
            />
            <Route
              path="parents/:id"
              element={
                <PeopleManagement key="parents-view" kind="parents" />
              }
            />
            <Route
              path="parents/:id/edit"
              element={
                <PeopleManagement key="parents-edit" kind="parents" />
              }
            />

            {/* Batches */}
            <Route
              path="batches"
              element={<BatchManagement key="batch-list" />}
            />
            <Route
              path="batches/new"
              element={<BatchManagement key="batch-new" />}
            />
            <Route
              path="batches/:id"
              element={<BatchManagement key="batch-view" />}
            />
            <Route
              path="batches/:id/edit"
              element={<BatchManagement key="batch-edit" />}
            />
            <Route
              path="events"
              element={<EventManagement key="events-list" />}
            />
            <Route
              path="events/new"
              element={<EventManagement key="events-new" />}
            />
            <Route
              path="events/:id"
              element={<EventManagement key="events-view" />}
            />
            <Route
              path="events/:id/edit"
              element={<EventManagement key="events-edit" />}
            />
            <Route path="fees" element={<FeeManagement />} />
          </Route>
        </Route>

        {/* Teacher dashboard */}
        <Route element={<ProtectedRoute allowedRole="TEACHER" />}>
          <Route
            path="/dashboard/teacher"
            element={<DashboardPage />}
          />
        </Route>

        {/* Student dashboard */}
        <Route element={<ProtectedRoute allowedRole="STUDENT" />}>
          <Route
            path="/dashboard/student"
            element={<DashboardPage />}
          />
        </Route>

        {/* Parent dashboard */}
        <Route element={<ProtectedRoute allowedRole="PARENT" />}>
          <Route
            path="/dashboard/parent"
            element={<DashboardPage />}
          />
        </Route>

        {/* Previous admin URL */}
        <Route
          path="/admin"
          element={<Navigate to="/dashboard/admin" replace />}
        />

        {/* Unknown URLs */}
        <Route
          path="*"
          element={
            <main>
              <h1>Page not found</h1>
              <Link to="/">Back to home</Link>
            </main>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}