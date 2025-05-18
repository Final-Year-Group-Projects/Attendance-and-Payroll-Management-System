package com.distributedproject.authservice.service;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Create a mock user with username and role
        testUser = new User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testGenerateTokenAndExtractRole() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);

        String extractedRole = jwtService.extractRole(token);
        assertEquals("ROLE_USER", extractedRole);
    }

    @Test
    void testInvalidTokenReturnsNull() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
        assertThrows(Exception.class, () -> jwtService.extractRole(invalidToken));
    }
}
