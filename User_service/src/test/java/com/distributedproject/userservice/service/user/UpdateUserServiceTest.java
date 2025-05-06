package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import com.distributedproject.userservice.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserService updateUserService;

    private User existingUser;
    private User userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating a mock existing user
        existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUserFullName("Alice Smith");
        existingUser.setUserType("Admin");
        existingUser.setUserAddress("123 Main St");
        existingUser.setUserTelephone("123-456-7890");

        // Creating mock user details to be updated
        userDetails = new User();
        userDetails.setUserFullName("Alice Johnson");
        userDetails.setUserType("User");
        userDetails.setUserAddress("456 Elm St");
        userDetails.setUserTelephone("098-765-4321");
    }

    @Test
    void updateUser_shouldUpdateUserWhenUserIsFound() {
        // Arrange: Mocking user repository to return an existing user
        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserFullNameIgnoreCase("Alice Johnson")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);  // Mocking save method to return the existingUser

        // Act: Calling the update method
        User updatedUser = updateUserService.updateUser(1L, userDetails);

        // Assert: Verifying the updated user details
        assertNotNull(updatedUser);
        assertEquals("Alice Johnson", updatedUser.getUserFullName());
        assertEquals("User", updatedUser.getUserType());
        assertEquals("456 Elm St", updatedUser.getUserAddress());
        assertEquals("098-765-4321", updatedUser.getUserTelephone());

        // Verify interactions with the repository
        verify(userRepository, times(1)).findByUserId(1L);
        verify(userRepository, times(1)).findByUserFullNameIgnoreCase("Alice Johnson");
        verify(userRepository, times(1)).save(any(User.class));  // Verifying save was called
    }

    @Test
    void updateUser_shouldThrowResponseStatusExceptionWhenUserNameIsTaken() {
        // Arrange: Mocking user repository to return an existing user and another user with the same name
        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(existingUser));
        User existingUserWithSameName = new User();
        existingUserWithSameName.setUserId(2L);
        existingUserWithSameName.setUserFullName("Alice Johnson");  // Same name as userDetails
        when(userRepository.findByUserFullNameIgnoreCase("Alice Johnson")).thenReturn(Optional.of(existingUserWithSameName));

        // Act & Assert: Calling the update method and expecting ResponseStatusException
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> updateUserService.updateUser(1L, userDetails)
        );

        // Assert: Verifying exception details
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User name is already taken.", exception.getReason());

        // Verify interactions with the repository
        verify(userRepository, times(1)).findByUserId(1L);
        verify(userRepository, times(1)).findByUserFullNameIgnoreCase("Alice Johnson");
    }
    @Test
    void updateUser_shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        // Arrange: Mocking user repository to return empty when user is not found
        when(userRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert: Calling the update method and expecting UserNotFoundException
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> updateUserService.updateUser(1L, userDetails)
        );

        // Assert: Verifying exception message
        assertEquals("User with ID 1 not found", exception.getMessage());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUserId(1L);
    }
}
