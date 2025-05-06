package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        User user = new User();
        user.setUserFullName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(1L);

        when(userRepository.existsByUserFullNameIgnoreCase("John Doe")).thenReturn(false);
        when(roleRepository.existsById("1")).thenReturn(true);
        when(departmentRepository.existsById("1")).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User result = createUserService.createUser(user);

        assertNotNull(result);
        assertEquals("John Doe", result.getUserFullName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_nameAlreadyExists_throwsException() {
        User user = new User();
        user.setUserFullName("Jane Doe");

        when(userRepository.existsByUserFullNameIgnoreCase("Jane Doe")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertEquals("400 BAD_REQUEST \"User full name already taken.\"", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_roleDoesNotExist_throwsException() {
        User user = new User();
        user.setUserFullName("Alice Smith");
        user.setRoleId(2L);
        user.setDepartmentId(3L);

        when(userRepository.existsByUserFullNameIgnoreCase("Alice Smith")).thenReturn(false);
        when(roleRepository.existsById("2")).thenReturn(false);
        when(departmentRepository.existsById("3")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Role ID 2 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_departmentDoesNotExist_throwsException() {
        User user = new User();
        user.setUserFullName("Bob Lee");
        user.setRoleId(4L);
        user.setDepartmentId(5L);

        when(userRepository.existsByUserFullNameIgnoreCase("Bob Lee")).thenReturn(false);
        when(roleRepository.existsById("4")).thenReturn(true);
        when(departmentRepository.existsById("5")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Department ID 5 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_roleAndDepartmentDoNotExist_throwsException() {
        User user = new User();
        user.setUserFullName("Charlie Kim");
        user.setRoleId(6L);
        user.setDepartmentId(7L);

        when(userRepository.existsByUserFullNameIgnoreCase("Charlie Kim")).thenReturn(false);
        when(roleRepository.existsById("6")).thenReturn(false);
        when(departmentRepository.existsById("7")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createUserService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Role ID 6 does not exist"));
        assertTrue(exception.getMessage().contains("Department ID 7 does not exist"));
        verify(userRepository, never()).save(any(User.class));
    }
}
