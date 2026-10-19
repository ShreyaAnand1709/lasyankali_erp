const BASE_URL = "http://localhost:8080";

export type Student = {
  studentId: number;
  firstName: string | null;
  lastName: string | null;
  username: string;
  email: string | null;
  mobileNumber: string | null;
  admissionNumber: string | null;
  gender: string | null;
  dateOfBirth: string | null;
  joiningDate: string | null;
  photoUrl: string | null;
  status: string;
};

export type StudentPayload = Record<string, string | null>;

async function readResponse(response: Response): Promise<unknown> {
  const text = await response.text();

  if (!text) return null;

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

async function request(
  path: string,
  options: RequestInit = {},
): Promise<unknown> {
  const method = options.method ?? "GET";
  const headers = new Headers(options.headers);

  if (!["GET", "HEAD", "OPTIONS"].includes(method.toUpperCase())) {
    const csrfResponse = await fetch(`${BASE_URL}/api/auth/csrf`, {
      credentials: "include",
      cache: "no-store",
    });

    if (!csrfResponse.ok) {
      throw new Error("Unable to obtain a security token. Please retry.");
    }

    const csrf: { headerName: string; token: string } =
      await csrfResponse.json();

    headers.set(csrf.headerName, csrf.token);
  }

  if (options.body) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers,
    credentials: "include",
    cache: "no-store",
  });

  const body = await readResponse(response);

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Your session has expired. Please log in again.");
    }

    const message =
      typeof body === "object" &&
      body !== null &&
      "message" in body &&
      typeof body.message === "string"
        ? body.message
        : typeof body === "string"
          ? body
          : `Request failed (${response.status}).`;

    throw new Error(message);
  }

  return body;
}

export async function getStudents(
  signal?: AbortSignal,
  keyword = "",
): Promise<Student[]> {
  const query = keyword.trim()
    ? `/search?${new URLSearchParams({ keyword: keyword.trim() })}`
    : "";

  return (await request(`/api/student${query}`, { signal })) as Student[];
}

export async function getStudent(
  id: string,
  signal?: AbortSignal,
): Promise<Student> {
  return (await request(`/api/student/${encodeURIComponent(id)}`, {
    signal,
  })) as Student;
}

export async function createStudent(
  payload: StudentPayload,
): Promise<void> {
  await request("/api/student", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateStudent(
  id: string,
  payload: StudentPayload,
): Promise<void> {
  await request(`/api/student/${encodeURIComponent(id)}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function deactivateStudent(id: string): Promise<void> {
  await request(`/api/student/${encodeURIComponent(id)}`, {
    method: "DELETE",
  });
}