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

        existingUser = new User();
        existingUser.setUserId("E101");  // Changed from 1L to "E101"
        existingUser.setUserFullName("Alice Smith");
        existingUser.setUserType("Admin");
        existingUser.setUserAddress("123 Main St");
        existingUser.setUserTelephone("123-456-7890");

        userDetails = new User();
        userDetails.setUserFullName("Alice Johnson");
        userDetails.setUserType("User");
        userDetails.setUserAddress("456 Elm St");
        userDetails.setUserTelephone("098-765-4321");
    }

    @Test
    void updateUser_shouldUpdateUserWhenUserIsFound() {
        when(userRepository.findByUserId("E101")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserFullNameIgnoreCase("Alice Johnson")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = updateUserService.updateUser("E101", userDetails);  // Changed input to "E101"

        assertNotNull(updatedUser);
        assertEquals("Alice Johnson", updatedUser.getUserFullName());
        assertEquals("User", updatedUser.getUserType());
        assertEquals("456 Elm St", updatedUser.getUserAddress());
        assertEquals("098-765-4321", updatedUser.getUserTelephone());

        verify(userRepository, times(1)).findByUserId("E101");
        verify(userRepository, times(1)).findByUserFullNameIgnoreCase("Alice Johnson");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowResponseStatusExceptionWhenUserNameIsTaken() {
        when(userRepository.findByUserId("E101")).thenReturn(Optional.of(existingUser));
        User existingUserWithSameName = new User();
        existingUserWithSameName.setUserId("E102");
        existingUserWithSameName.setUserFullName("Alice Johnson");
        when(userRepository.findByUserFullNameIgnoreCase("Alice Johnson")).thenReturn(Optional.of(existingUserWithSameName));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> updateUserService.updateUser("E101", userDetails)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User name is already taken.", exception.getReason());

        verify(userRepository, times(1)).findByUserId("E101");
        verify(userRepository, times(1)).findByUserFullNameIgnoreCase("Alice Johnson");
    }

    @Test
    void updateUser_shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        when(userRepository.findByUserId("E101")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> updateUserService.updateUser("E101", userDetails)
        );

        assertEquals("User with ID E101 not found", exception.getMessage());

        verify(userRepository, times(1)).findByUserId("E101");
    }
}