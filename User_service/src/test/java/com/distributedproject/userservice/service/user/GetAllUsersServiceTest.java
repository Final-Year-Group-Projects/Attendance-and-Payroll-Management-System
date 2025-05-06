package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllUsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetAllUsersService getAllUsersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange: Creating mock users
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUserFullName("Alice Smith");
        user1.setUserType("Admin");
        user1.setUserAddress("123 Main St");
        user1.setUserTelephone("123-456-7890");
        user1.setDepartmentId(1L);
        user1.setRoleId(1L);

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUserFullName("Bob Johnson");
        user2.setUserType("User");
        user2.setUserAddress("456 Elm St");
        user2.setUserTelephone("098-765-4321");
        user2.setDepartmentId(2L);
        user2.setRoleId(2L);

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act: Calling the service method
        List<User> users = getAllUsersService.getAllUsers();

        // Assert: Validating results
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Alice Smith", users.get(0).getUserFullName());
        assertEquals("Bob Johnson", users.get(1).getUserFullName());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findAll();
    }
}
