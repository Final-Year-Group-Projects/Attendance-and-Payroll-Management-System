package com.attendance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> validationResponse = restTemplate.exchange(
                    "http://auth-service:8080/auth/validate",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token validation failed");
                return;
            }

            Map<String, Object> responseBody = validationResponse.getBody();
            String role = (String) responseBody.get("role");

            String path = request.getRequestURI();
            String method = request.getMethod();

            String normalizedRole = role != null ? role.trim().toLowerCase() : "";

            // Admin-only endpoint
            if (path.matches("^/attendance/leaves/\\d+/status$") && !"admin".equals(normalizedRole) && !"super_admin".equals(normalizedRole)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access denied: Admins only permission");
                return;
            }

            // All other attendance endpoints require Employee or Admin role
            if (!"admin".equals(normalizedRole) && !"super_admin".equals(normalizedRole) && !"employee".equals(normalizedRole)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access denied: insufficient permissions");
                return;
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
