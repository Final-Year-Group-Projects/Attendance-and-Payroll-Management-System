package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.service.department.GetUserDepartmentByIdService;
import com.distributedproject.userservice.service.user.GetUserRoleByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GetUserDepartmentByIdController {

    private static final Logger logger = LoggerFactory.getLogger(GetUserDepartmentByIdController.class);

    @Autowired
    private GetUserDepartmentByIdService userDepartmentService;

    @GetMapping("/get/userdepartment/{userId}")
    public ResponseEntity<String> getUserDepartmentById(@PathVariable String userId) {
        // Log the request to get the user Department by ID
        logger.info("Received request to get department for user with ID: {}", userId);

        // Attempt to retrieve the Department by user ID
        String roleName = userDepartmentService.getUserDepartmentById(userId)
                .orElseThrow(() -> {
                    logger.error("Department for user with ID: {} not found", userId);  // Log error when role is not found
                    return new UserNotFoundException(userId);
                });

        // Log the successful retrieval of the Department
        logger.info("Department for user with ID: {} retrieved successfully: {}", userId, roleName);

        // Return the Department name in the response
        return ResponseEntity.ok(roleName);
    }
}
