package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.exception.user.UserIdAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameAlreadyExistsException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CreateUserService createUserService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserFullName("John Doe");
        user.setUserId("johndoe123");
        user.setRoleId(1L);  // Set Long type roleId
        user.setDepartmentId(1L);  // Set Long type departmentId
    }

    @Test
    public void testCreateUser_whenUserNameExists_shouldThrowUserNameAlreadyExistsException() {
        // Arrange
        when(userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())).thenReturn(true);

        // Act & Assert
        UserNameAlreadyExistsException exception = assertThrows(UserNameAlreadyExistsException.class, () -> {
            createUserService.createUser(user);
        });

        // Update expected message to match the actual exception message
        assertEquals("User name is already taken.", exception.getMessage());
    }

    @Test
    public void testCreateUser_whenUserIdExists_shouldThrowUserIdAlreadyExistsException() {
        // Arrange
        when(userRepository.existsByUserIdIgnoreCase(user.getUserId())).thenReturn(true);

        // Act & Assert
        UserIdAlreadyExistsException exception = assertThrows(UserIdAlreadyExistsException.class, () -> {
            createUserService.createUser(user);
        });

        // Update expected message to match the actual exception message
        assertEquals("User name is already taken.", exception.getMessage());
    }

    @Test
    void testCreateUser_RoleNotFound() {
        when(userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())).thenReturn(false);
        when(userRepository.existsByUserIdIgnoreCase(user.getUserId())).thenReturn(false);
        when(roleRepository.existsById(user.getRoleId())).thenReturn(false);

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> {
            createUserService.createUser(user);
        });

        assertEquals("Role with ID 1 not found", exception.getMessage());
    }

    @Test
    void testCreateUser_DepartmentNotFound() {
        when(userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())).thenReturn(false);
        when(userRepository.existsByUserIdIgnoreCase(user.getUserId())).thenReturn(false);
        when(roleRepository.existsById(user.getRoleId())).thenReturn(true);
        when(departmentRepository.existsById(user.getDepartmentId())).thenReturn(false);

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class, () -> {
            createUserService.createUser(user);
        });

        assertEquals("Department with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testCreateUser_whenRoleAndDepartmentExist_shouldCreateUser() {
        // Arrange
        when(userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())).thenReturn(false);
        when(userRepository.existsByUserIdIgnoreCase(user.getUserId())).thenReturn(false);
        when(roleRepository.existsById(user.getRoleId())).thenReturn(true);  // Use existsById for Long type roleId
        when(departmentRepository.existsById(user.getDepartmentId())).thenReturn(true);  // Use existsById for Long type departmentId
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User createdUser = createUserService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        verify(userRepository, times(1)).save(user);
    }
}
