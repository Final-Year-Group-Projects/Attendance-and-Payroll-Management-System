package com.distributedproject.authservice.service;

import com.distributedproject.authservice.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public ResponseEntity<String> logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        tokenBlacklistService.blacklistToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}
