package com.distributedproject.userservice.controller;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.CreateUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateUserController {

    @Autowired
    private CreateUserService userService;

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        System.out.println("Received user: " + user);
        return userService.createUser(user);
    }
}
