const BASE_URL = "http://localhost:8080";

export type Fee = {
  feeId: number;
  studentId: number;
  studentName: string;
  batchId: number;
  batchName: string;
  batchLevel: string;
  feeAmount: number | string;
  billingMonth: string;
  dueDate: string;
  feeStatus: string;
  remarks: string | null;
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

  const text = await response.text();
  let body: unknown = text;

  if (text) {
    try {
      body = JSON.parse(text);
    } catch {
      // Supports plain-text error responses.
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

export async function getFees(signal?: AbortSignal): Promise<Fee[]> {
  return (await request("/api/fees", { signal })) as Fee[];
}

export async function generateIndividualFee(
  studentId: number,
  batchId: number,
  month: string,
): Promise<boolean> {
  const result = await request("/api/fees/generate", {
    method: "POST",
    body: JSON.stringify({
      studentId,
      batchId,
      billingMonth: `${month}-01`,
    }),
  });

  if (typeof result !== "boolean") {
    throw new Error(
      "The fee-generation API must return true or false. Refresh the fee list before retrying.",
    );
  }

  return result;
}

export async function generateMonthlyFees(month: string): Promise<number> {
  const query = new URLSearchParams({
    billingMonth: `${month}-01`,
  });

  return (await request(`/api/fees/generate-monthly?${query}`, {
    method: "POST",
  })) as number;
}

export async function markOverdueFees(): Promise<number> {
  return (await request("/api/fees/mark-overdue", {
    method: "POST",
  })) as number;
}