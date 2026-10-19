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

async function getCsrf(): Promise<CsrfResponse> {
  const response = await fetch(`${AUTH_URL}/csrf`, {
    credentials: "include",
    cache: "no-store",
  });

  if (!response.ok) {
    throw await readError(response);
  }

  return response.json();
}

export async function login(
  username: string,
  password: string,
): Promise<LoginResponse> {
  const csrf = await getCsrf();

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

export async function logout(): Promise<void> {
  const csrf = await getCsrf();

  const response = await fetch(`${AUTH_URL}/logout`, {
    method: "POST",
    credentials: "include",
    headers: {
      [csrf.headerName]: csrf.token,
    },
  });

  if (!response.ok) {
    throw await readError(response);
  }
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