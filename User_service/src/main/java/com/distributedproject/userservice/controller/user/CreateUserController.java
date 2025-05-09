package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.exception.user.UserIdAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameAlreadyExistsException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.CreateUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CreateUserController {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserController.class);

    @Autowired
    private CreateUserService userService;

    @PostMapping("/create/users")
    public User createUser(@Valid @RequestBody User user) {
        logger.info("Received user creation request: {}", user);
        return userService.createUser(user);
    }

}
