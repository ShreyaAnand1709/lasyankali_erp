package com.lasyankali.erp.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret_key;
    private final UserRepository userRepository;

    private static final long JWT_EXPIRATION =
            1000L * 60 * 60 * 24;

    public JwtService(
            @Value("${jwt.secret}") String secret_key,
            UserRepository userRepository) {

        this.secret_key = secret_key;
        this.userRepository = userRepository;
    }

    private SecretKey signInKey() {
        // Preserve the existing signing-key derivation.
        byte[] keyBytes = Base64.getEncoder().encode(
                secret_key.getBytes(StandardCharsets.UTF_8));

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(
            String username,
            long authVersion) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required to generate a token");
        }

        if (authVersion < 0) {
            throw new IllegalArgumentException(
                    "Authentication version cannot be negative");
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(
                issuedAt.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(username)
                .claim("authVersion", authVersion)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(signInKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        Long tokenAuthVersion =
                claims.get("authVersion", Long.class);

        if (username == null
                || username.isBlank()
                || !username.equals(userDetails.getUsername())
                || expiration == null
                || !expiration.after(new Date())
                || !userDetails.isEnabled()
                || tokenAuthVersion == null
                || tokenAuthVersion < 0) {

            return false;
        }

        return userRepository.findByUserName(username)
                .map(user ->
                        Boolean.TRUE.equals(user.getIsActive())
                        && user.getAuthVersion()
                                == tokenAuthVersion.longValue())
                .orElse(false);
    }
}