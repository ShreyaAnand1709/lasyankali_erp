package com.lasyankali.erp.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.LoginRequestDTO;
import com.lasyankali.erp.dto.LoginResponseDTO;
import com.lasyankali.erp.dto.RegisterUserDTO;
import com.lasyankali.erp.security.JwtService;
import com.lasyankali.erp.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authservice;
    private final JwtService jwtService;
    private final CookieCsrfTokenRepository csrfTokenRepository;
    private final boolean secureCookie;

    public AuthController(
            AuthService authservice,
            JwtService jwtService,
            CookieCsrfTokenRepository csrfTokenRepository,
            @Value("${app.auth-cookie-secure:false}") boolean secureCookie) {

        this.authservice = authservice;
        this.jwtService = jwtService;
        this.csrfTokenRepository = csrfTokenRepository;
        this.secureCookie = secureCookie;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterUserDTO registerUserDTO) {

        authservice.registeredUser(registerUserDTO);
        return ResponseEntity.ok("Successfully registered");
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(Map.of(
                        "headerName", csrfToken.getHeaderName(),
                        "token", csrfToken.getToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletRequest request,
            HttpServletResponse servletResponse) {

        LoginResponseDTO response =
                authservice.loginResponseDTO(loginRequestDTO);

        String jwtToken = response.getToken();

        Duration remainingLifetime = Duration.between(
                Instant.now(),
                jwtService.extractExpiration(jwtToken).toInstant());

        ResponseCookie cookie = authCookie(jwtToken, remainingLifetime);

        csrfTokenRepository.saveToken(null, request, servletResponse);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            Authentication authentication) {

        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Account role is missing"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(Map.of(
                        "username", authentication.getName(),
                        "role", role));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        SecurityContextHolder.clearContext();
        csrfTokenRepository.saveToken(null, request, response);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        authCookie("", Duration.ZERO).toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(Map.of("message", "Logged out successfully"));
    }

    private ResponseCookie authCookie(
            String value,
            Duration maxAge) {

        return ResponseCookie.from("AUTH_TOKEN", value)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}