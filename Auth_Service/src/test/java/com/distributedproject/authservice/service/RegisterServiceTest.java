package com.distributedproject.authservice.service;

import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        // Given
        User inputUser = new User();
        inputUser.setUsername("testuser");
        inputUser.setPassword("plainpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User savedUser = registerService.register(inputUser);

        // Then
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedpassword", savedUser.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterFailsWhenUsernameExists() {
        // Given
        User inputUser = new User();
        inputUser.setUsername("existinguser");
        inputUser.setPassword("anyPassword");

        when(userRepository.findByUsername("existinguser"))
                .thenReturn(Optional.of(new User()));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registerService.register(inputUser));

        assertEquals("Username is already taken!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
