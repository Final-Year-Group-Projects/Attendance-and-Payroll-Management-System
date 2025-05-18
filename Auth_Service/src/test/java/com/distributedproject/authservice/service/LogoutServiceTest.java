package com.distributedproject.authservice.service;

import com.distributedproject.authservice.repository.TokenBlacklistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test
    }

    @Test
    void testLogout_Successful() {
        // Given
        String authHeader = "Bearer validToken";

        // When
        ResponseEntity<String> response = logoutService.logout(authHeader);

        // Then
        verify(tokenBlacklistService, times(1)).blacklistToken("validToken");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully", response.getBody());
    }

    @Test
    void testLogout_MissingAuthHeader() {
        // Given
        String authHeader = null;

        // When
        ResponseEntity<String> response = logoutService.logout(authHeader);

        // Then
        verify(tokenBlacklistService, never()).blacklistToken(anyString());
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Missing or invalid Authorization header", response.getBody());
    }

    @Test
    void testLogout_InvalidAuthHeader() {
        // Given
        String authHeader = "InvalidHeader";

        // When
        ResponseEntity<String> response = logoutService.logout(authHeader);

        // Then
        verify(tokenBlacklistService, never()).blacklistToken(anyString());
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Missing or invalid Authorization header", response.getBody());
    }

    @Test
    void testLogout_ValidToken_BlacklistCalled() {
        // Given
        String authHeader = "Bearer tokenToBlacklist";

        // When
        ResponseEntity<String> response = logoutService.logout(authHeader);

        // Then
        verify(tokenBlacklistService).blacklistToken("tokenToBlacklist");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully", response.getBody());
    }
}
