package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.service.user.DeleteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DeleteUserController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserController.class);

    @Autowired
    private DeleteUserService userService;

    @DeleteMapping("/delete/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        // Log the deletion request with the userId
        logger.info("Received request to delete user with ID: {}", userId);

        // Perform the deletion
        userService.deleteUser(userId);

        // Log the successful deletion
        logger.info("User with ID: {} deleted successfully", userId);

        return ResponseEntity.ok("User deleted successfully");
    }
}
