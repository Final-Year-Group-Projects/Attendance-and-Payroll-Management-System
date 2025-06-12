package com.attendance;

import com.attendance.client.UserServiceClient;
import com.attendance.client.UserServiceClient.EmployeeDTO;
import com.attendance.client.UserServiceClient.UserDTO;
import com.attendance.client.UserServiceClient.AuthRequest;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public UserServiceClient userServiceClient() {
        UserServiceClient mockClient = Mockito.mock(UserServiceClient.class);

        // Mock getUserById
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId("E001"); // Changed from 1L to "E001"
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setRole("ADMIN");
        Mockito.when(mockClient.getUserById("E001")).thenReturn(employeeDTO); // Changed from 1L to "E001"

        // Mock validateUser
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("admin");
        userDTO.setRole("ADMIN");
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("admin");
        authRequest.setPassword("admin123");
        Mockito.when(mockClient.validateUser(authRequest)).thenReturn(userDTO);

        return mockClient;
    }
}