package com.lasyankali.erp.security;

import java.security.Key;
import io.jsonwebtoken.Claims;
import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
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
	
	private Claims extractAllClaims(
	        String token) {
	    return Jwts.parser()
	            .verifyWith(
	                    (javax.crypto.SecretKey)
	                            signInKey())
	            .build()
	            .parseSignedClaims(token)
	           .getPayload();
	}
	// extracting the jwt token and validatng it using the username and the claims
	public String extractUsername(String token) { 
		return extractAllClaims(token).getSubject();
	}
	
	public Date extractExpiration(String token) {
	    return extractAllClaims(token)
	            .getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
	    return extractExpiration(token)
	            .before(new Date());
	}
	public boolean isTokenValid(String token,UserDetails userDetails) {
	    String username =extractUsername(token);
	    return username.equals(userDetails.getUsername())
	            &&
	            !isTokenExpired(token);
	}
	
	
}

