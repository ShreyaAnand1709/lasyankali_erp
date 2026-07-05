package com.lasyankali.erp.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class jwtService {
	private static final String SECRET_KEY = "myVerySecureSecretKeyForLasyankaliERPProject123456";
	private static final long JWT_EXPIRATION = 1000*60*60*24;
	
	private Key signInKey() {
		byte[] keyBytes = Base64.getEncoder().
				encode(SECRET_KEY.getBytes());
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateToken(String username) {
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(
						System.currentTimeMillis() + JWT_EXPIRATION))
				.signWith(signInKey()).compact();
	}
}
