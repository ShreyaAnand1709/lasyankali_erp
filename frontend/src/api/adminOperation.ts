const BASE_URL = "http://localhost:8080";

export type Participant = {
  studentId: number;
  studentName: string;
  eventName: string;
  registrationDate: string;
  status: string | null;
};

async function readError(response: Response): Promise<Error> {
  const body = await response.json().catch(() => null);

  return new Error(
    body?.message || `Request failed (${response.status}).`,
  );
}

export async function getParticipants(
  eventId: string,
  signal?: AbortSignal,
): Promise<Participant[]> {
  const response = await fetch(
    `${BASE_URL}/api/events/${encodeURIComponent(eventId)}/participants`,
    {
      credentials: "include",
      cache: "no-store",
      signal,
    },
  );

  if (!response.ok) {
    throw await readError(response);
  }

  return response.json();
}

export async function updateParticipation(
  eventId: string,
  studentId: number,
  status: string,
): Promise<void> {
  const csrfResponse = await fetch(`${BASE_URL}/api/auth/csrf`, {
    credentials: "include",
    cache: "no-store",
  });

  if (!csrfResponse.ok) {
    throw await readError(csrfResponse);
  }

  const csrf: {
    headerName: string;
    token: string;
  } = await csrfResponse.json();

  const response = await fetch(
    `${BASE_URL}/api/events/${encodeURIComponent(eventId)}/student/${studentId}`,
    {
      method: "PATCH",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        [csrf.headerName]: csrf.token,
      },
      body: JSON.stringify({ status }),
    },
  );

  if (!response.ok) {
    throw await readError(response);
  }
}