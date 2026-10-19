const BASE = "http://localhost:8080";

export type Batch = {
  batchId: number;
  batchCode: string;
  batchName: string;
  discipline: string;
  level: string;
  startTime: string;
  endTime: string;
  capacity: number;
  status: string;
};

export type BatchMember = {
  memberId: number;
  firstName: string;
  lastName: string;
  username: string;
  profileStatus: string;
  enrollmentStatus: string | null;
  assignmentDate: string | null;
};

export type BatchPayload = {
  batchCode?: string;
  batchName: string;
  discipline: string;
  level: string;
  startTime: string;
  endTime: string;
  capacity: number;
  status?: string;
};

async function request(
  path: string,
  options: RequestInit = {},
): Promise<unknown> {
  const method = (options.method ?? "GET").toUpperCase();
  const headers = new Headers(options.headers);

  if (!["GET", "HEAD", "OPTIONS"].includes(method)) {
    const csrfResponse = await fetch(`${BASE}/api/auth/csrf`, {
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
      // Creation may return plain text.
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

export async function getBatches(
  keyword = "",
  signal?: AbortSignal,
): Promise<Batch[]> {
  const query = keyword.trim()
    ? `/search?${new URLSearchParams({ keyword: keyword.trim() })}`
    : "";

  return (await request(`/api/batches${query}`, { signal })) as Batch[];
}

export async function getBatch(
  id: string,
  signal?: AbortSignal,
): Promise<Batch> {
  return (await request(
    `/api/batches/${encodeURIComponent(id)}`,
    { signal },
  )) as Batch;
}

export async function saveBatch(
  payload: BatchPayload,
  id?: string,
): Promise<void> {
  await request(
    `/api/batches${id ? `/${encodeURIComponent(id)}` : ""}`,
    {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    },
  );
}

export async function deactivateBatch(id: string): Promise<void> {
  await request(`/api/batches/${encodeURIComponent(id)}`, {
    method: "DELETE",
  });
}

export async function getBatchMembers(
  id: string,
  kind: "students" | "teachers",
  signal?: AbortSignal,
): Promise<BatchMember[]> {
  return (await request(
    `/api/batches/${encodeURIComponent(id)}/${kind}`,
    { signal },
  )) as BatchMember[];
}

export async function changeBatchMember(
  id: string,
  kind: "students" | "teachers",
  memberId: string,
  remove = false,
): Promise<void> {
  await request(
    `/api/batches/${encodeURIComponent(id)}/${kind}/${encodeURIComponent(memberId)}`,
    { method: remove ? "DELETE" : "POST" },
  );
}