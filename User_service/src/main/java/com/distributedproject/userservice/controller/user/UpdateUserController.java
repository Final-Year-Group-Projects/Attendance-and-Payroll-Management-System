package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.UpdateUserService;  // Import the service class
import com.distributedproject.userservice.exception.user.UserNotFoundException;  // Import custom exception
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UpdateUserController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserController.class);  // Create logger instance

    @Autowired
    private UpdateUserService updateUserService;  // Autowire the correct service class

    @PutMapping("/user/update/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @Valid @RequestBody User userDetails) {
        // Log the incoming request
        logger.info("Received request to update user with userId: {}", userId);

        // Call the service to update the user details
        User updatedUser = updateUserService.updateUser(String.valueOf(userId), userDetails);

        // If user is null or not found, throw exception (UserNotFoundException)
        if (updatedUser == null) {
            logger.error("User with userId: {} not found", userId);  // Log an error if the user is not found
            throw new UserNotFoundException(userId);  // Trigger custom exception
        }

        // Log the successful user update
        logger.info("User with userId: {} successfully updated", userId);

        // Return the updated user as a response
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
