package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.service.JwtService;
import com.distributedproject.authservice.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ValidateController {

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

        // Check if token is blacklisted
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }

        try {
            // Extract username and role from token
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            // Return username and role as a response
            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "role", role
            ));
        } catch (Exception e) {
            // Return error if token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
