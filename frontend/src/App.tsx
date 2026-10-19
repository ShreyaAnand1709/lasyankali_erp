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
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminOverview from "./pages/AdminOverview";
import StudentManagement from "./pages/StudentManagement";
import PeopleManagement from "./pages/PeopleManagement";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute allowedRole="ADMIN" />}>
        
        <Route path="/dashboard/admin" element={<AdminDashboard />}>
          <Route index element={<AdminOverview />} />
          <Route path="students" element={<StudentManagement />} />
          <Route path="students/new" element={<StudentManagement />} />
          <Route path="students/:id" element={<StudentManagement />} />
          <Route path="students/:id/edit" element={<StudentManagement />} />
          

          <Route
              path="teachers"
              element={<PeopleManagement key="teachers-list" kind="teachers" />}
            />
            <Route
              path="teachers/new"
              element={<PeopleManagement key="teachers-new" kind="teachers" />}
            />
            <Route
              path="teachers/:id"
              element={<PeopleManagement key="teachers-view" kind="teachers" />}
            />
            <Route
              path="teachers/:id/edit"
              element={<PeopleManagement key="teachers-edit" kind="teachers" />}
            />

            <Route
              path="parents"
              element={<PeopleManagement key="parents-list" kind="parents" />}
            />
            <Route
              path="parents/new"
              element={<PeopleManagement key="parents-new" kind="parents" />}
            />
            <Route
              path="parents/:id"
              element={<PeopleManagement key="parents-view" kind="parents" />}
            />
            <Route
              path="parents/:id/edit"
              element={<PeopleManagement key="parents-edit" kind="parents" />}
            />
        </Route>
        </Route>

        <Route element={<ProtectedRoute allowedRole="TEACHER" />}>
          <Route
            path="/dashboard/teacher"
            element={<DashboardPage />}
          />
        </Route>

        <Route element={<ProtectedRoute allowedRole="STUDENT" />}>
          <Route
            path="/dashboard/student"
            element={<DashboardPage />}
          />
        </Route>

        <Route element={<ProtectedRoute allowedRole="PARENT" />}>
          <Route
            path="/dashboard/parent"
            element={<DashboardPage />}
          />
        </Route>

        <Route
          path="/admin"
          element={<Navigate to="/dashboard/admin" replace />}
        />

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