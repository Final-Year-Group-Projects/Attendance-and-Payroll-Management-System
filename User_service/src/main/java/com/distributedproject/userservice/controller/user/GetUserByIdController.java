package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.GetUserByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GetUserByIdController {

    private static final Logger logger = LoggerFactory.getLogger(GetUserByIdController.class);

    @Autowired
    private GetUserByIdService userService;

    @GetMapping("/get/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        // Log the request to get the user by ID
        logger.info("Received request to get user with ID: {}", userId);

        // Attempt to retrieve the user by ID
        User user = userService.getUserById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);  // Log error when user is not found
                    return new UserNotFoundException(userId);
                });

        // Log the successful retrieval of the user
        logger.info("User with ID: {} retrieved successfully", userId);

        // Return the user in the response
        return ResponseEntity.ok(user);
    }
}
