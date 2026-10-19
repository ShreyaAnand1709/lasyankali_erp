export type DashboardSection =
  | "overview"
  | "events"
  | "fees"
  | "students"
  | "batches";

export type DashboardRecord =
  Record<string, string | number | null>;

export async function getDashboardRecords(
  role: string,
  section: DashboardSection,
  signal?: AbortSignal,
): Promise<DashboardRecord[]> {
  let path: string;

  switch (section) {
    case "events":
      path = "/api/events";
      break;

    case "fees":
      if (role === "STUDENT") {
        path = "/api/fees/feeMe";
      } else if (role === "PARENT") {
        path = "/api/fees/my-children";
      } else {
        throw new Error("This fee section is not available for your role.");
      }
      break;

    case "students":
      if (role !== "TEACHER") {
        throw new Error("This section is not available for your role.");
      }
      path = "/api/student";
      break;

    case "batches":
      if (role !== "TEACHER") {
        throw new Error("This section is not available for your role.");
      }
      path = "/api/batches";
      break;

    default:
      return [];
  }

  const response = await fetch(`http://localhost:8080${path}`, {
    credentials: "include",
    cache: "no-store",
    signal,
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Your session has expired. Please log in again.");
    }

    const body = await response.json().catch(() => null);

    throw new Error(
      body?.message || `Unable to load records (${response.status}).`,
    );
  }

  return response.json();
}