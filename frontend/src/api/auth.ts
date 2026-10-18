const AUTH_URL = "http://localhost:8080/api/auth";

export type AuthUser = {
  username: string;
  role: string;
};

type LoginResponse = AuthUser & {
  message: string;
};

type CsrfResponse = {
  headerName: string;
  token: string;
};

async function readError(response: Response): Promise<Error> {
  const body = await response.json().catch(() => null);

  return new Error(
    body?.message ?? `Request failed (${response.status})`,
  );
}

export async function login(
  username: string,
  password: string,
): Promise<LoginResponse> {
  // First obtain a CSRF token and its associated cookie.
  const csrfResponse = await fetch(`${AUTH_URL}/csrf`, {
    credentials: "include",
    cache: "no-store",
  });

  if (!csrfResponse.ok) {
    throw await readError(csrfResponse);
  }

  const csrf: CsrfResponse = await csrfResponse.json();

  // Send credentials with the CSRF header and browser cookies.
  const response = await fetch(`${AUTH_URL}/login`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      [csrf.headerName]: csrf.token,
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw await readError(response);
  }

  return response.json();
}

export async function getCurrentUser(): Promise<AuthUser | null> {
  const response = await fetch(`${AUTH_URL}/me`, {
    credentials: "include",
    cache: "no-store",
  });

  if (response.status === 401) {
    return null;
  }

  if (!response.ok) {
    throw await readError(response);
  }

  return response.json();
}