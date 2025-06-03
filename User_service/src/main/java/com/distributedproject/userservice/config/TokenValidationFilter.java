package com.distributedproject.userservice.config;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
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
            ResponseEntity<Map> validationResponse = restTemplate
                    .exchange("http://api-gateway/auth/validate", HttpMethod.POST, entity, Map.class);

            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                HttpStatus status = (HttpStatus) validationResponse.getStatusCode(); // Cast to HttpStatus
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token validation failed: " + status.getReasonPhrase());
                return;
            }

            Map<String, Object> responseBody = validationResponse.getBody();

            String tokenUserId = (String) responseBody.get("userId");  // Extract userId from token
            String role = (String) responseBody.get("role");

            // Store info in request attributes for downstream use if needed
            request.setAttribute("userId", tokenUserId);
            request.setAttribute("role", role);

            String path = request.getRequestURI();

            // Admin and Super_Admin can access everything
            if (!"Admin".equals(role) && !"Super_Admin".equals(role)) {
                boolean isAllowed = false;

                if ("Employee".equals(role)) {
                    // Allow GET endpoints
                    boolean isGetAllowed = path.matches("^/user/get/users/.*") || path.equals("/user/search");

                    // Allow UPDATE only if token userId matches path userId
                    boolean isUpdateOwnInfo = false;
                    if (path.matches("^/user/update/users/.+")) {
                        String[] pathParts = path.split("/");
                        String pathUserId = pathParts[pathParts.length - 1];  // treat as String

                        isUpdateOwnInfo = tokenUserId.equals(pathUserId);
                    }

                    isAllowed = isGetAllowed || isUpdateOwnInfo;
                }

                if (!isAllowed) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access denied: insufficient permissions");
                    return;
                }
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
