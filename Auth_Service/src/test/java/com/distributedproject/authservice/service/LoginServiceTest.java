package com.distributedproject.authservice.service;

import com.distributedproject.authservice.dto.LoginRequest;
import com.distributedproject.authservice.exception.InvalidCredentialsException;
import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginService loginService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");
    }

    @Test
    void testLoginSuccess() {
        // Given
        LoginRequest loginRequest = new LoginRequest("testUser", "password");

        // Mock UserRepository to return a user
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Mock PasswordEncoder to return true for password match
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        // Mock JwtService to return a token
        String mockedToken = "mockedJwtToken";
        when(jwtService.generateToken(any())).thenReturn(mockedToken);

        // When
        Map<String, String> result = loginService.login(loginRequest);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals("mockedJwtToken", result.get("accessToken"), "Access token should be returned");
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void testLoginInvalidUsername() {
        // Given
        LoginRequest loginRequest = new LoginRequest("invalidUser", "password");

        // Mock UserRepository to return an empty Optional
        when(userRepository.findByUsername("invalidUser")).thenReturn(Optional.empty());

        // When & Then
        InvalidCredentialsException thrown = assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });
        assertEquals("Invalid username", thrown.getMessage(), "Exception message should match");
        verify(userRepository, times(1)).findByUsername("invalidUser");
        verify(passwordEncoder, times(0)).matches(any(), any());
        verify(jwtService, times(0)).generateToken(any());
    }

    @Test
    void testLoginInvalidPassword() {
        // Given
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");

        // Mock UserRepository to return a user
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Mock PasswordEncoder to return false for password mismatch
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // When & Then
        InvalidCredentialsException thrown = assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });
        assertEquals("Invalid password", thrown.getMessage(), "Exception message should match");
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
        verify(jwtService, times(0)).generateToken(any());
    }
}
