package com.distributedproject.userservice.controller;

import com.distributedproject.userservice.exception.UserNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.GetUserByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetUserByIdController {

    @Autowired
    private GetUserByIdService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(user);
    }
}
