package com.example.PayrollService.filter;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

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
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token validation failed");
                return;
            }

            Map<String, Object> responseBody = validationResponse.getBody();
            String role = (String) responseBody.get("role");
            String username = (String) responseBody.get("username");
            String tokenUserId = (String) responseBody.get("userId");

            request.setAttribute("role", role);
            request.setAttribute("username", username);
            request.setAttribute("userId", tokenUserId);

            String path = request.getRequestURI();
            String method = request.getMethod();

            String normalizedRole = role != null ? role.trim().toLowerCase() : "";
            if (!"admin".equals(normalizedRole) && !"super_admin".equals(normalizedRole)) {
                boolean isAllowed = false;

                if ("employee".equals(normalizedRole)) {
                    // Allow POST reimbursements (no employeeId in path)
                    if ("/payrolls/reimbursements".equals(path) && "POST".equals(method)) {
                        isAllowed = true;
                    }
                    // Allow payslip HTML/PDF (no employeeId in path, just role check)
                    else if (("GET".equals(method)) &&
                            (path.matches("^/payrolls/\\d+/payslip$") || path.matches("^/payrolls/\\d+/payslip/pdf$"))) {
                        isAllowed = true;
                    }

                    // All other paths must have matching employeeId
                    else {
                        // Extract employeeId from URL
                        String extractedEmployeeId = extractEmployeeIdFromPath(path);
                        if (extractedEmployeeId != null && extractedEmployeeId.equals(tokenUserId)) {
                            isAllowed = (
                                    (path.matches("^/payrolls/employee/[a-zA-Z0-9]+$") && "GET".equals(method)) ||
                                            (path.matches("^/payrolls/[a-zA-Z0-9]+/notify$") && "POST".equals(method)) ||
                                            (path.matches("^/payrolls/reimbursements/employee/[a-zA-Z0-9]+$") && "GET".equals(method))
                            );
                        }
                    }
                }
                if (!isAllowed) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access denied: insufficient permissions");
                    return;
                }
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation failed due to an internal error. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    //Extract employeeId from path
    private String extractEmployeeIdFromPath(String path) {
        String[] regexes = {
                "^/payrolls/employee/([a-zA-Z0-9]+)$",
                "^/payrolls/([a-zA-Z0-9]+)/notify$",
                "^/payrolls/reimbursements/employee/([a-zA-Z0-9]+)$"
        };

        for (String regex : regexes) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
