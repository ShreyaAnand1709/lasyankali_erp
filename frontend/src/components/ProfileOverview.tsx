import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import "../css/ProfileOverview.css";

type Profile = {
  firstName: string | null;
  lastName: string | null;
  username: string;
  email: string | null;
  mobileNumber: string | null;

  admissionNumber?: string | null;
  dateOfBirth?: string | null;
  gender?: string | null;
  joiningDate?: string | null;
  status?: string | null;

  employeeCode?: string | null;
  specialization?: string | null;
  qualification?: string | null;
  yearsOfExperience?: number | null;

  occupation?: string | null;
  address?: string | null;
};

type Detail = {
  label: string;
  value: string | number | null | undefined;
};

const profilePaths: Record<string, string> = {
  STUDENT: "/api/student/me",
  TEACHER: "/api/teachers/me",
  PARENT: "/api/parents/me",
};

const roleNames: Record<string, string> = {
  STUDENT: "Student",
  TEACHER: "Teacher",
  PARENT: "Parent",
};

function displayValue(value: Detail["value"]) {
  return value === null || value === undefined || value === ""
    ? "Not provided"
    : String(value);
}

export default function ProfileOverview() {
  const { user } = useAuth();

  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [attempt, setAttempt] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    async function loadProfile() {
      setLoading(true);
      setError("");
      setProfile(null);

      try {
        const path = profilePaths[user?.role ?? ""];

        if (!path) {
          throw new Error("Profile access is unavailable for this role.");
        }

        const response = await fetch(
          `http://localhost:8080${path}`,
          {
            credentials: "include",
            cache: "no-store",
            signal: controller.signal,
          },
        );

        if (!response.ok) {
          const body = await response.json().catch(() => null);

          throw new Error(
            response.status === 401
              ? "Your session has expired. Please log in again."
              : body?.message ||
                `Unable to load your profile (${response.status}).`,
          );
        }

        const result: Profile = await response.json();

        if (!controller.signal.aborted) {
          setProfile(result);
        }
      } catch (exception) {
        if (!controller.signal.aborted) {
          setError(
            exception instanceof Error
              ? exception.message
              : "Unable to load your profile.",
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    void loadProfile();

    return () => controller.abort();
  }, [user?.username, user?.role, attempt]);

  if (loading) {
    return (
      <div className="profile-message" role="status">
        Loading your profile…
      </div>
    );
  }

  if (error || !profile) {
    return (
      <div className="profile-error" role="alert">
        <p>{error || "Your profile is unavailable."}</p>
        <button
          type="button"
          onClick={() => setAttempt((previous) => previous + 1)}
        >
          Retry
        </button>
      </div>
    );
  }

  const role = user?.role ?? "";

  const fullName =
    [profile.firstName, profile.lastName]
      .filter(Boolean)
      .join(" ") || profile.username;

  const initials =
    [profile.firstName, profile.lastName]
      .map((name) => name?.trim().charAt(0) ?? "")
      .join("")
      .toUpperCase() ||
    profile.username.slice(0, 2).toUpperCase();

  const personalDetails: Detail[] = [
    { label: "Full name", value: fullName },
    { label: "Username", value: profile.username },
    { label: "Email", value: profile.email },
    { label: "Mobile number", value: profile.mobileNumber },
  ];

  const academyDetails: Detail[] =
    role === "STUDENT"
      ? [
          { label: "Admission number", value: profile.admissionNumber },
          { label: "Date of birth", value: profile.dateOfBirth },
          { label: "Gender", value: profile.gender },
          { label: "Joining date", value: profile.joiningDate },
          { label: "Profile status", value: profile.status },
        ]
      : role === "TEACHER"
        ? [
            { label: "Employee code", value: profile.employeeCode },
            { label: "Specialization", value: profile.specialization },
            { label: "Qualification", value: profile.qualification },
            {
              label: "Experience",
              value:
                profile.yearsOfExperience == null
                  ? null
                  : `${profile.yearsOfExperience} years`,
            },
            { label: "Joining date", value: profile.joiningDate },
            { label: "Profile status", value: profile.status },
          ]
        : [
            { label: "Occupation", value: profile.occupation },
            { label: "Address", value: profile.address },
          ];

  function renderDetails(details: Detail[]) {
    return (
      <dl className="profile-fields">
        {details.map((detail) => (
          <div key={detail.label}>
            <dt>{detail.label}</dt>
            <dd>{displayValue(detail.value)}</dd>
          </div>
        ))}
      </dl>
    );
  }

  return (
    <section
      className="profile-overview"
      aria-labelledby="profile-name"
    >
      <header className="profile-banner">
        <div className="profile-avatar" aria-hidden="true">
          {initials}
        </div>

        <div className="profile-identity">
          <p className="profile-eyebrow">Your academy profile</p>
          <h2 id="profile-name">{fullName}</h2>
          <p>{profile.email || profile.username}</p>
        </div>

        <span className="profile-role">
          {roleNames[role]}
        </span>
      </header>

      <div className="profile-sections">
        <section>
          <h3>Personal details</h3>
          {renderDetails(personalDetails)}
        </section>

        <section>
          <h3>
            {role === "PARENT" ? "Additional details" : "Academy details"}
          </h3>
          {renderDetails(academyDetails)}
        </section>
      </div>

      <p className="profile-note">
        Contact your academy administrator to correct your details.
      </p>
    </section>
  );
}