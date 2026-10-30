import { createPaymentOrder, verifyPayment } from "../api/payment";
import type { VerificationResponse } from "../api/payment";

type CheckoutResponse = {
  razorpay_order_id: string;
  razorpay_payment_id: string;
  razorpay_signature: string;
};

type CheckoutOptions = {
  key: string;
  amount: number;
  currency: string;
  name: string;
  description: string;
  order_id: string;
  theme: { color: string };
  retry: { enabled: boolean };
  modal: { ondismiss: () => void };
  handler: (response: CheckoutResponse) => void;
};

type CheckoutInstance = {
  open: () => void;
  close: () => void;
  on: (
    event: "payment.failed",
    callback: (response: {
      error?: { description?: string };
    }) => void,
  ) => void;
};

declare global {
  interface Window {
    Razorpay?: new (options: CheckoutOptions) => CheckoutInstance;
  }
}

let checkoutScript: Promise<void> | null = null;

function loadCheckout(): Promise<void> {
  if (window.Razorpay) return Promise.resolve();
  if (checkoutScript) return checkoutScript;

  checkoutScript = new Promise<void>((resolve, reject) => {
    const script = document.createElement("script");

    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.async = true;

    script.onload = () => {
      if (window.Razorpay) {
        resolve();
      } else {
        script.remove();
        checkoutScript = null;
        reject(new Error("Payment checkout could not be initialized."));
      }
    };

    script.onerror = () => {
      script.remove();
      checkoutScript = null;
      reject(new Error("Unable to load checkout. Check your connection."));
    };

    document.body.appendChild(script);
  });

  return checkoutScript;
}

export async function payFee(
  feeId: number,
): Promise<VerificationResponse | null> {
  await loadCheckout();

  const order = await createPaymentOrder(feeId);

  const Razorpay = window.Razorpay;

  if (!Razorpay) {
    throw new Error("Payment checkout is unavailable.");
  }

  return new Promise<VerificationResponse | null>((resolve, reject) => {
    let verifying = false;
    let finished = false;

    const checkout = new Razorpay({
      key: order.keyId,
      amount: order.amountInPaise,
      currency: order.currency,
      name: "Lasyankali: NrityaSutra",
      description: `Academy fee #${order.feeId}`,
      order_id: order.providerOrderId,
      theme: { color: "#481526" },
      retry: { enabled: false },

      modal: {
        ondismiss: () => {
          if (!verifying && !finished) {
            finished = true;
            resolve(null);
          }
        },
      },

      handler: (payment) => {
        if (verifying || finished) return;
        verifying = true;

        void verifyPayment({
          providerOrderId: payment.razorpay_order_id,
          providerPaymentId: payment.razorpay_payment_id,
          providerSignature: payment.razorpay_signature,
        })
          .then((result) => {
            finished = true;

            if (
              result.paymentStatus !== "SUCCESS" ||
              result.feeStatus !== "PAID"
            ) {
              reject(
                new Error(
                  "Payment confirmation is pending. Refresh the fee list before trying again.",
                ),
              );
              return;
            }

            resolve(result);
          })
          .catch(() => {
            finished = true;
            reject(
              new Error(
                "Checkout completed, but server verification could not be confirmed. " +
                  "Do not pay again yet. Refresh the fee list and contact the administrator " +
                  `with order ${payment.razorpay_order_id} and payment ${payment.razorpay_payment_id}.`,
              ),
            );
          });
      },
    });

    checkout.on("payment.failed", (response) => {
      if (finished || verifying) return;

      finished = true;
      checkout.close();

      reject(
        new Error(
          response.error?.description ??
            "Payment failed. Refresh the fee list before retrying.",
        ),
      );
    });

    checkout.open();
  });
}