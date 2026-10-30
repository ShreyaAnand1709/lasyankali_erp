package com.lasyankali.erp.service;

public interface PasswordResetService {
	void requestPasswordReset(String email);
	void resetPassword(String token, String newPassword);
}
