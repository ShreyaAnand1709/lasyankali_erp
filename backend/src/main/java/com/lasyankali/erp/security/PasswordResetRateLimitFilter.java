package com.lasyankali.erp.security;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class PasswordResetRateLimitFilter extends OncePerRequestFilter{
	private static final int MAX_CLIENTS = 10_000;

    private static final long WINDOW_NANOS =
            Duration.ofMinutes(15).toNanos();

    private final Map<String, Window> windows = new HashMap<>();

    private static class Window {

        private final long startedAt;
        private int requests;

        private Window(long startedAt) {
            this.startedAt = startedAt;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getServletPath();

        return !"/api/auth/forgot-password".equals(path)
                && !"/api/auth/reset-password".equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        int limit = "/api/auth/forgot-password".equals(path)
                ? 5
                : 20;

        String key = request.getRemoteAddr() + "|" + path;

        long retryAfter = reserveRequest(key, limit);

        if (retryAfter > 0) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader(
                    "Retry-After",
                    Long.toString(retryAfter));

            response.getWriter().write(
                    "{\"code\":\"TOO_MANY_REQUESTS\","
                    + "\"message\":\"Too many password reset attempts. "
                    + "Please try again later.\"}");

            return;
        }

        filterChain.doFilter(request, response);
    }

    private synchronized long reserveRequest(
            String key,
            int limit) {

        long now = System.nanoTime();

        windows.entrySet().removeIf(
                entry ->
                        now - entry.getValue().startedAt
                                >= WINDOW_NANOS);

        Window window = windows.get(key);

        if (window == null) {
            if (windows.size() >= MAX_CLIENTS) {
                return 60;
            }

            window = new Window(now);
            windows.put(key, window);
        }

        if (window.requests >= limit) {
            long remaining =
                    WINDOW_NANOS - (now - window.startedAt);

            return Math.max(
                    1L,
                    TimeUnit.NANOSECONDS.toSeconds(remaining) + 1L);
        }

        window.requests++;
        return 0;
    }
}
