package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.GetUserByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetUserByIdController {

    @Autowired
    private GetUserByIdService userService;

    @GetMapping("/get/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userService.getUserById(String.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(user);
    }
}
