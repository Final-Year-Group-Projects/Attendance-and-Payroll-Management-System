package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // Act
        deleteUserService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_userNotFound_throwsException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> deleteUserService.deleteUser(userId)
        );

        assertEquals("User with ID 1 not found", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
}
