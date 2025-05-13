package com.attendance.client;

import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public EmployeeDTO getUserById(Long id) {
        if (id == null || id.equals(999L)) {
            return null; // Simulate non-existent employee
        }
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(id);
        employee.setFirstName("Fallback");
        employee.setLastName("User");
        employee.setEmail("fallback@" + id + ".com");
        if (id.equals(1L)) {
            employee.setRole("Admin"); // Simulate Admin role for employeeId = 1
        } else {
            employee.setRole("Employee"); // Default role for others
        }
        return employee;
    }

    @Override
    public UserDTO validateUser(AuthRequest authRequest) {
        UserDTO user = new UserDTO();
        if ("admin".equalsIgnoreCase(authRequest.getUsername()) && "password123".equals(authRequest.getPassword())) {
            user.setUsername("admin");
            user.setRole("Admin");
        } else if ("employee".equalsIgnoreCase(authRequest.getUsername()) && "password123".equals(authRequest.getPassword())) {
            user.setUsername("employee");
            user.setRole("Employee");
        } else {
            return null; // Simulate invalid credentials
        }
        return user;
    }
}