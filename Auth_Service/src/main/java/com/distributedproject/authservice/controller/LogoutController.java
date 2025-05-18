package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.service.LogoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        logger.info("Received logout request with Authorization header: {}", authHeader);

        ResponseEntity<String> response = logoutService.logout(authHeader);

        logger.info("Logout successful. Response: {}", response.getBody());

        return response;
    }
}
