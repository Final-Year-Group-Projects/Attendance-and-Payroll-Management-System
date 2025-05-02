package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        System.out.println("Received user: " + user);
        return registerService.register(user);
    }
}
