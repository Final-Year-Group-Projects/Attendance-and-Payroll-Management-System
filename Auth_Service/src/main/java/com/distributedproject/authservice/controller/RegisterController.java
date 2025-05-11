package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.exception.UsernameAlreadyExistsException;
import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.repository.UserRepository;
import com.distributedproject.authservice.service.RegisterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        logger.info("Received registration request for username: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }

        User savedUser = registerService.register(user);

        logger.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }
}
