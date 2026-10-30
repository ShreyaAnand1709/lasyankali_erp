package com.lasyankali.erp.service.Impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;

import com.lasyankali.erp.entity.PasswordResetToken;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.repository.PasswordResetTokenRepository;
import com.lasyankali.erp.repository.UserRepository;
import com.lasyankali.erp.service.EmailService;
import com.lasyankali.erp.service.PasswordResetService;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{

	private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final String resetPageUrl;
    private final int expiryMinutes;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService,
            @Value("${app.password-reset.page-url}") String resetPageUrl,
            @Value("${app.password-reset.expiry-minutes:15}") int expiryMinutes, PasswordEncoder passwordEncoder) {

        if (expiryMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Password reset expiry must be greater than zero");
        }

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.resetPageUrl = resetPageUrl;
        this.expiryMinutes = expiryMinutes;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElse(null);

        // Do not reveal whether an account exists or is inactive.
        if (user == null || !Boolean.TRUE.equals(user.getIsActive())) {
            return;
        }

        String rawToken = generateToken();

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setTokenHash(hashToken(rawToken));
        resetToken.setCreatedAt(now);
        resetToken.setExpiresAt(now.plusMinutes(expiryMinutes));
        resetToken.setUsedAt(null);

        tokenRepository.saveAndFlush(resetToken);

        String separator = resetPageUrl.contains("?") ? "&" : "?";
        String resetLink = resetPageUrl
                + separator
                + "token="
                + rawToken;

        emailService.sendPasswordResetMail(
                user.getEmail(),
                user.getFirstName(),
                resetLink);
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable",
                    exception);
        }
    }

	@Override
	@Transactional
	public void resetPassword(String token, String newPassword) {
		// TODO Auto-generated method stub
		if (token == null
	            || !token.matches("[A-Za-z0-9_-]{43}")) {
	        throw invalidResetLink();
	    }

	    if (newPassword == null
	            || newPassword.isBlank()
	            || newPassword.length() < 12
	            || newPassword.length() > 72
	            || newPassword.getBytes(StandardCharsets.UTF_8).length > 72) {

	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "Password must contain 12–72 characters "
	                + "and must not exceed 72 UTF-8 bytes");
	    }

	    String tokenHash = hashToken(token);

	    Long userId = tokenRepository.findUserIdByTokenHash(tokenHash)
	            .orElseThrow(this::invalidResetLink);

	    // Always lock the account before locking its reset token.
	    User user = userRepository.findByIdForUpdate(userId)
	            .orElseThrow(this::invalidResetLink);

	    PasswordResetToken resetToken =
	            tokenRepository.findByTokenHashForUpdate(tokenHash)
	                    .orElseThrow(this::invalidResetLink);

	    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

	    if (!Boolean.TRUE.equals(user.getIsActive())
	            || !userId.equals(resetToken.getUser().getUserId())
	            || resetToken.getUsedAt() != null
	            || resetToken.getExpiresAt() == null
	            || !resetToken.getExpiresAt().isAfter(now)) {

	        throw invalidResetLink();
	    }

	    user.setPasswordHash(passwordEncoder.encode(newPassword));
	    user.setAuthVersion(Math.addExact(user.getAuthVersion(), 1L));
	    user.setUpdatedAt(now);

	    resetToken.setUsedAt(now);

	    userRepository.save(user);
	    tokenRepository.save(resetToken);

	    // Other previously issued links must not reset the password again.
	    tokenRepository.invalidateUnusedTokensForUser(userId, now);
	}

	private ResponseStatusException invalidResetLink() {
	    return new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "This password reset link is invalid or expired. "
	            + "Please request a new link.");
	}

}
