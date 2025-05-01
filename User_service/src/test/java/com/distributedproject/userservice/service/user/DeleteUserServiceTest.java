package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUserServiceTest {

    private UserRepository userRepository;
    private DeleteUserService deleteUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deleteUserService = new DeleteUserService();

        // Use reflection to inject the mock repository
        try {
            var userField = DeleteUserService.class.getDeclaredField("userRepository");
            userField.setAccessible(true);
            userField.set(deleteUserService, userRepository);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteUser_successfulDelete() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById("1")).thenReturn(true);

        // Act
        deleteUserService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteUser_userNotFound_throwsException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById("1")).thenReturn(false);

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> deleteUserService.deleteUser(userId)
        );

        assertEquals("User with ID 1 not found", exception.getMessage());
        verify(userRepository, never()).deleteById(anyString());
    }
}
