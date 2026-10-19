const BASE_URL = "http://localhost:8080";

export type AcademyEvent = {
  eventId: number;
  eventCode: string;
  eventName: string;
  eventType: string;
  eventDate: string;
  venue: string;
  description: string | null;
  status: string;
};

export type EventPayload = {
  eventCode?: string;
  eventName: string;
  eventType: string;
  eventDate: string;
  venue: string;
  description: string | null;
  status?: string;
};

async function request(
  path: string,
  options: RequestInit = {},
): Promise<unknown> {
  const headers = new Headers(options.headers);
  const method = (options.method ?? "GET").toUpperCase();

  if (!["GET", "HEAD", "OPTIONS"].includes(method)) {
    const csrfResponse = await fetch(`${BASE_URL}/api/auth/csrf`, {
      credentials: "include",
      cache: "no-store",
    });

    if (!csrfResponse.ok) {
      throw new Error("Unable to obtain a security token.");
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
      typeof body.message === "string" &&
      body.message
        ? body.message
        : typeof body === "string" && body
          ? body
          : `Request failed (${response.status}).`;

    throw new Error(message);
  }

  return body;
}

export async function getEvents(
  keyword = "",
  signal?: AbortSignal,
): Promise<AcademyEvent[]> {
  const query = keyword.trim()
    ? `/search?${new URLSearchParams({ keyword: keyword.trim() })}`
    : "";

  return (await request(`/api/events${query}`, {
    signal,
  })) as AcademyEvent[];
}

export async function getEvent(
  id: string,
  signal?: AbortSignal,
): Promise<AcademyEvent> {
  return (await request(`/api/events/${encodeURIComponent(id)}`, {
    signal,
  })) as AcademyEvent;
}

export async function saveEvent(
  payload: EventPayload,
  id?: string,
): Promise<void> {
  await request(
    `/api/events${id ? `/${encodeURIComponent(id)}` : ""}`,
    {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    },
  );
}

export async function cancelEvent(id: string): Promise<void> {
  await request(`/api/events/${encodeURIComponent(id)}`, {
    method: "DELETE",
  });
}