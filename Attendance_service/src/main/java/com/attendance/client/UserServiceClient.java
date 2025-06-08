package com.attendance.client;

import com.attendance.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "USER-SERVICE", fallback = UserServiceClientFallback.class, configuration = FeignClientConfiguration.class)
public interface UserServiceClient {
    @GetMapping("/user/get/users/{userId}")
    EmployeeDTO getUserById(@PathVariable("userId") String userId);

    @PostMapping("/auth/validate")
    UserDTO validateUser(AuthRequest authRequest);

    class EmployeeDTO {
        private String id; // Remains String
        private String firstName;
        private String lastName;
        private String email;
        private String role;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    class UserDTO {
        private String username;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    class AuthRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}