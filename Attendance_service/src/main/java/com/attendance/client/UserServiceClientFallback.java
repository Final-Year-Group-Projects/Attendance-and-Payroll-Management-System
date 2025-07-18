package com.attendance.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallback.class);


    @Override
    public EmployeeDTO getUserById(String id) {
        logger.info("Fallback getUserById called with id: {}", id);
        if (id == null || "999".equals(id)) { // Compare as String instead of Long
            logger.info("Returning null for id: {}", id);
            return null;
        }
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(id); // Set as String
        employee.setFirstName("Fallback");
        employee.setLastName("User");
        employee.setEmail("fallback@" + id + ".com");
        if ("2".equals(id)) { // Compare as String instead of Long
            employee.setRole("Admin");
            logger.info("Set role to Admin for id: {}", id);
        } else {
            employee.setRole("Employee");
            logger.info("Set role to Employee for id: {}", id);
        }
        logger.info("Returning EmployeeDTO with role: {} for id: {}", employee.getRole(), id);
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
            return null;
        }
        return user;
    }
}