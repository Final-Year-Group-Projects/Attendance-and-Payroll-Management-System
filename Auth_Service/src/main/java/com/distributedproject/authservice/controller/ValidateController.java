package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.service.JwtService;
import com.distributedproject.authservice.service.TokenBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ValidateController {

    private static final Logger logger = LoggerFactory.getLogger(ValidateController.class);

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public ValidateController(JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        logger.info("Received token validation request");

        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            logger.warn("Token is blacklisted: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }

        try {
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);
            logger.info("Token validated successfully. Username: {}, Role: {}", username, role);

            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "role", role
            ));
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
