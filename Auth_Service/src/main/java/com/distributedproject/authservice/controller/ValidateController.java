package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ValidateController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token); // Make sure extractRole exists in JwtService

            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
