package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private DepartmentRepository departmentRepository;
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        departmentRepository = mock(DepartmentRepository.class);

        createUserService = new CreateUserService();

        // Use reflection to inject the mock repositories
        try {
            var userField = CreateUserService.class.getDeclaredField("userRepository");
            userField.setAccessible(true);
            userField.set(createUserService, userRepository);

            var roleField = CreateUserService.class.getDeclaredField("roleRepository");
            roleField.setAccessible(true);
            roleField.set(createUserService, roleRepository);

            var departmentField = CreateUserService.class.getDeclaredField("departmentRepository");
            departmentField.setAccessible(true);
            departmentField.set(createUserService, departmentRepository);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUser_successfulSave() {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(1L);

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(roleRepository.existsById("1")).thenReturn(true);
        when(departmentRepository.existsById("1")).thenReturn(true);
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

    @Test
    void createUser_roleDoesNotExist_throwsException() {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(1L);

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(roleRepository.existsById("1")).thenReturn(false);
        when(departmentRepository.existsById("1")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Role ID 1 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_departmentDoesNotExist_throwsException() {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(1L);

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(roleRepository.existsById("1")).thenReturn(true);
        when(departmentRepository.existsById("1")).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Department ID 1 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_roleAndDepartmentDoNotExist_throwsException() {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(1L);

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(roleRepository.existsById("1")).thenReturn(false);
        when(departmentRepository.existsById("1")).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Role ID 1 does not exist"));
        assertTrue(exception.getMessage().contains("Department ID 1 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }
}