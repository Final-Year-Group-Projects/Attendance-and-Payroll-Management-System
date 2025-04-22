package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.UpdateUserService;  // Import the service class
import com.distributedproject.userservice.exception.user.UserNotFoundException;  // Import custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateUserController {

    @Autowired
    private UpdateUserService updateUserService;  // Autowire the correct service class

    @PutMapping("/update/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        // Now we throw UserNotFoundException if the user doesn't exist
        User updatedUser = updateUserService.updateUser(userId, userDetails);

        // If user is null or not found, throw exception (UserNotFoundException)
        if (updatedUser == null) {
            throw new UserNotFoundException(userId);  // Trigger custom exception
        }

        // If user is found and updated
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

