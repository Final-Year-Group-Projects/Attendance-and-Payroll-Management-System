package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserServiceTest {

    private UserRepository userRepository;
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        createUserService = new CreateUserService();
        // Use reflection to inject the mock repository
        var field = CreateUserService.class.getDeclaredFields()[0];
        field.setAccessible(true);
        try {
            field.set(createUserService, userRepository);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUser_successfulSave() {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = createUserService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getUserName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_nameAlreadyExists_throwsException() {
        // Arrange
        User user = new User();
        user.setUserName("Jane Doe");

        when(userRepository.existsByUserFullNameIgnoreCase("Jane Doe")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertEquals("400 BAD_REQUEST \"User full name already taken.\"", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
