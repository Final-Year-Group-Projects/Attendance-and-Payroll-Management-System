package com.example.PayrollService.feign;

import com.example.PayrollService.dto.integration.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserServiceClientFallback implements UserServiceClient {
    @Override
    public UserDTO getUserDetails(String employeeId) {
        // Return fallback data
        return new UserDTO(employeeId, getFallbackRole(employeeId));
    }

    private String getFallbackRole(String employeeId) {
        // Your fallback role logic here
        Map<String, String> fallbackRoles = Map.of(
                "1", "ENGINEER",
                "2", "MANAGER",
                "3", "HR"
        );
        return fallbackRoles.getOrDefault(employeeId, "ENGINEER");
    }
}