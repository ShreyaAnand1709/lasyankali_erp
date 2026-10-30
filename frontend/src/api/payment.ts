const BASE = "http://localhost:8080";

export type PaymentOrder = {
  paymentId: number;
  feeId: number;
  providerOrderId: string;
  amountInPaise: number;
  currency: string;
  keyId: string;
};

export type VerificationRequest = {
  providerOrderId: string;
  providerPaymentId: string;
  providerSignature: string;
};

export type VerificationResponse = {
  paymentId: number;
  feeId: number;
  providerOrderId: string;
  providerPaymentId: string;
  paymentStatus: string;
  feeStatus: string;
  message: string;
};

async function post<T>(path: string, body?: unknown): Promise<T> {
  const csrfResponse = await fetch(`${BASE}/api/auth/csrf`, {
    credentials: "include",
    cache: "no-store",
  });

  if (!csrfResponse.ok) {
    throw new Error("Unable to obtain the security token. Please retry.");
  }

  const csrf: { headerName: string; token: string } =
    await csrfResponse.json();

  const headers = new Headers({
    [csrf.headerName]: csrf.token,
  });

  if (body !== undefined) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${BASE}${path}`, {
    method: "POST",
    credentials: "include",
    headers,
    body: body === undefined ? undefined : JSON.stringify(body),
  });

  const result = await response.json().catch(() => null);

  if (!response.ok) {
    throw new Error(
      result?.message ??
        (response.status === 401
          ? "Your session has expired. Please log in again."
          : `Payment request failed (${response.status}).`),
    );
  }

  if (result === null) {
    throw new Error("The server returned an empty payment response.");
  }

  return result as T;
}

export function createPaymentOrder(feeId: number): Promise<PaymentOrder> {
  return post<PaymentOrder>(`/api/payments/fees/${feeId}/order`);
}

export function verifyPayment(
  payload: VerificationRequest,
): Promise<VerificationResponse> {
  return post<VerificationResponse>("/api/payments/verify", payload);
}