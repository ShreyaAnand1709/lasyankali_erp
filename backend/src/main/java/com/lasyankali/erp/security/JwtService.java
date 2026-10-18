package com.lasyankali.erp.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret_key;

    private static final long JWT_EXPIRATION =
            1000L * 60 * 60 * 24;

    public JwtService(@Value("${jwt.secret}") String secret_key) {
        this.secret_key = secret_key;
    }

    private SecretKey signInKey() {
        // Retains the current key derivation for token compatibility.
        byte[] keyBytes = Base64.getEncoder().encode(
                secret_key.getBytes(StandardCharsets.UTF_8));

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required to generate a token");
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(
                issuedAt.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(username)
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

        return username != null
                && !username.isBlank()
                && username.equals(userDetails.getUsername())
                && expiration != null
                && expiration.after(new Date())
                && userDetails.isEnabled();
    }
}