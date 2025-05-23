package com.distributedproject.userservice.config;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("token", token), headers);

        try {
            ResponseEntity<Map> validationResponse = restTemplate
                    .postForEntity("http://auth-service:8080/auth/validate", entity, Map.class);

            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token validation failed");
                return;
            }

            Map<String, Object> responseBody = validationResponse.getBody();
            String username = (String) responseBody.get("username");
            String role = (String) responseBody.get("role");

            request.setAttribute("username", username);
            request.setAttribute("role", role);

            String path = request.getRequestURI();

            // Admin can access everything
            if (!"Admin".equals(role) && !"Super_Admin".equals(role)) {
                boolean isEmployeeAllowed = false;

                if ("Employee".equals(role)) {
                    boolean isGetAllowed =
                            path.matches("^/user/get/users/.*") || path.equals("/user/search");

                    boolean isUpdateOwnInfo = false;

                    if (path.matches("^/user/update/users/\\d+$")) {
                        // Extract userId from path
                        String[] pathParts = path.split("/");
                        Long pathUserId = Long.parseLong(pathParts[pathParts.length - 1]);

                        // Fetch actual user ID using username
                        Optional<User> optionalUser = userRepository.findByUserFullNameIgnoreCase(username);
                        if (optionalUser.isPresent()) {
                            String actualUserId = optionalUser.get().getUserId();
                            isUpdateOwnInfo = actualUserId.equals(pathUserId);
                        }
                    }

                    isEmployeeAllowed = isGetAllowed || isUpdateOwnInfo;
                }

                if (!isEmployeeAllowed) {
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
