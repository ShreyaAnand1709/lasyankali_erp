export type PersonKind = "teachers" | "parents";
export type Person = Record<string, string | number | null>;
export type PersonPayload = Record<string, string | number | null>;

const BASE = "http://localhost:8080";

async function request(
  path: string,
  options: RequestInit = {},
): Promise<unknown> {
  const headers = new Headers(options.headers);
  const method = (options.method ?? "GET").toUpperCase();

  if (!["GET", "HEAD", "OPTIONS"].includes(method)) {
    const csrfResponse = await fetch(`${BASE}/api/auth/csrf`, {
      credentials: "include",
      cache: "no-store",
    });

    if (!csrfResponse.ok) {
      throw new Error("Unable to obtain the security token. Please retry.");
    }

    const csrf: { headerName: string; token: string } =
      await csrfResponse.json();

    headers.set(csrf.headerName, csrf.token);
  }

  if (options.body) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${BASE}${path}`, {
    ...options,
    headers,
    credentials: "include",
    cache: "no-store",
  });

  const text = await response.text();
  let body: unknown = text;

  if (text) {
    try {
      body = JSON.parse(text);
    } catch {
      // Some endpoints return plain text.
    }
  }

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
        : typeof body === "string" && body
          ? body
          : `Request failed (${response.status}).`;

    throw new Error(message);
  }

  return body;
}

export async function listPeople(
  kind: PersonKind,
  keyword: string,
  signal?: AbortSignal,
): Promise<Person[]> {
  const query = keyword.trim()
    ? `/search?${new URLSearchParams({ keyword: keyword.trim() })}`
    : "";

  return (await request(`/api/${kind}${query}`, { signal })) as Person[];
}

export async function getPerson(
  kind: PersonKind,
  id: string,
  signal?: AbortSignal,
): Promise<Person> {
  return (await request(
    `/api/${kind}/${encodeURIComponent(id)}`,
    { signal },
  )) as Person;
}

export async function savePerson(
  kind: PersonKind,
  payload: PersonPayload,
  id?: string,
): Promise<void> {
  await request(
    `/api/${kind}${id ? `/${encodeURIComponent(id)}` : ""}`,
    {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    },
  );
}

export async function deactivateTeacher(id: string): Promise<void> {
  await request(`/api/teachers/${encodeURIComponent(id)}`, {
    method: "DELETE",
  });
}