const BASE = "http://localhost:8080";

async function submit(
  path: string,
  body: Record<string, string>,
): Promise<string> {
  const csrfResponse = await fetch(`${BASE}/api/auth/csrf`, {
    credentials: "include",
    cache: "no-store",
  });

  if (!csrfResponse.ok) {
    throw new Error("Unable to obtain a security token. Please retry.");
  }

  const csrf: { headerName: string; token: string } =
    await csrfResponse.json();

  const response = await fetch(`${BASE}/api/auth/${path}`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      [csrf.headerName]: csrf.token,
    },
    body: JSON.stringify(body),
  });

  const result = await response.json().catch(() => null);

  if (!response.ok) {
    if (response.status === 429) {
      throw new Error("Too many attempts. Please try again later.");
    }

    throw new Error(
      result?.message ?? `Request failed (${response.status}).`,
    );
  }

  return result?.message ?? "Request completed successfully.";
}

export function requestPasswordReset(email: string): Promise<string> {
  return submit("forgot-password", { email });
}

export function resetPassword(
  token: string,
  newPassword: string,
): Promise<string> {
  return submit("reset-password", { token, newPassword });
}