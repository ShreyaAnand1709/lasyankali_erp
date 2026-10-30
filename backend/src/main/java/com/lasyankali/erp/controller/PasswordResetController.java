package com.lasyankali.erp.controller;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.ForgotPasswordRequestDTO;
import com.lasyankali.erp.dto.ResetPasswordRequestDTO;
import com.lasyankali.erp.service.PasswordResetService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {
	private static final Logger logger =
            LoggerFactory.getLogger(PasswordResetController.class);

    private final PasswordResetService passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService) {

        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {

        try {
            passwordResetService.requestPasswordReset(
                    request.getEmail());
        } catch (MailException exception) {
            // Keep the public response independent of account existence.
            // Do not log the email, reset link or token.
            logger.warn("Password reset email delivery failed");
        }

        return ResponseEntity.accepted().body(
                Map.of(
                        "message",
                        "If an active account exists for this email, "
                        + "a password reset link will be sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword());

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password reset successfully. "
                        + "Please log in with your new password."));
    }
}
