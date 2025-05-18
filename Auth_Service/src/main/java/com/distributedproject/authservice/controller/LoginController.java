package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.dto.LoginRequest;
import com.distributedproject.authservice.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for user: {}", loginRequest.getUsername());

        Map<String, String> response = loginService.login(loginRequest);

        logger.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }
}
