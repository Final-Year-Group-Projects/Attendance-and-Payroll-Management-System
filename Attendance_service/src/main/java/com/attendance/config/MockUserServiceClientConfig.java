package com.attendance.config;

import com.attendance.client.UserServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MockUserServiceClientConfig {

    //@Bean
    public UserServiceClient userServiceClient() {
        return new UserServiceClient() {
            @Override
            public EmployeeDTO getUserById(String id) {
                EmployeeDTO employee = new EmployeeDTO();
                employee.setId(id);
                employee.setFirstName("Mock");
                employee.setLastName("User");
                employee.setEmail("mock.user@example.com");
                employee.setRole("EMPLOYEE");
                return employee;
            }

            @Override
            public UserDTO validateUser(AuthRequest authRequest) {
                UserDTO user = new UserDTO();
                user.setUsername(authRequest.getUsername());
                user.setRole("EMPLOYEE");
                return user;
            }
        };
    }
}