package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.service.user.GetUserRoleByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GetUserRoleByIdController {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRoleByIdController.class);

    @Autowired
    private GetUserRoleByIdService userRoleService;

    @GetMapping("/user/get/userrole/{userId}")
    public ResponseEntity<String> getUserRoleById(@PathVariable String userId) {
        // Log the request to get the user role by ID
        logger.info("Received request to get role for user with ID: {}", userId);

        // Attempt to retrieve the role by user ID
        String roleName = userRoleService.getUserRoleById(userId)
                .orElseThrow(() -> {
                    logger.error("Role for user with ID: {} not found", userId);  // Log error when role is not found
                    return new UserNotFoundException(userId);
                });

        // Log the successful retrieval of the role
        logger.info("Role for user with ID: {} retrieved successfully: {}", userId, roleName);

        // Return the role name in the response
        return ResponseEntity.ok(roleName);
    }
}
