import { useCallback, useEffect, useState } from "react";
import type { ReactNode } from "react";

import { getCurrentUser } from "../api/auth";
import type { AuthUser } from "../api/auth";
import { AuthContext } from "./AuthContext";

type AuthProviderProps = {
  children: ReactNode;
};

export default function AuthProvider({
  children,
}: AuthProviderProps) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refreshUser = useCallback(async (): Promise<AuthUser | null> => {
    setLoading(true);
    setError(null);

    try {
      const currentUser = await getCurrentUser();
      setUser(currentUser);
      return currentUser;
    } catch (exception) {
      setUser(null);

      setError(
        exception instanceof Error
          ? exception.message
          : "Unable to check your session.",
      );

      throw exception;
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void refreshUser().catch(() => {
      // The error is exposed through authentication context.
    });
  }, [refreshUser]);

  return (
    <AuthContext.Provider
      value={{ user, loading, error, refreshUser }}
    >
      {children}
    </AuthContext.Provider>
  );
}