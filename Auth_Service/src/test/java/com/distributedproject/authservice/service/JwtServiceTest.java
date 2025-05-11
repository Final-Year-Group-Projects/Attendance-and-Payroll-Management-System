package com.distributedproject.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
    }

    @Test
    void testGenerateToken() {
        // Arrange
        UserDetails userDetails = new User("john", "password", Arrays.asList(() -> "ROLE_USER"));

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.contains("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"));  // Ensure token is generated (this is a simple check)
    }

    @Test
    void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken(new User("john", "password", Arrays.asList(() -> "ROLE_USER")));

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("john", username);
    }

    @Test
    void testExtractRole() {
        // Arrange
        String token = jwtService.generateToken(new User("john", "password", Arrays.asList(() -> "ROLE_USER")));

        // Act
        String role = jwtService.extractRole(token);

        // Assert
        assertEquals("ROLE_USER", role);
    }

    @Test
    void testExtractRole_whenNoAuthorities() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("john")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256), SignatureAlgorithm.HS256)
                .compact();

        // Act
        String role = jwtService.extractRole(token);

        // Assert
        assertNull(role);
    }
}
