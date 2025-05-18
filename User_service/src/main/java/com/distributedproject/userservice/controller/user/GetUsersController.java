package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.GetAllUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class GetUsersController {

    private static final Logger logger = LoggerFactory.getLogger(GetUsersController.class);

    @Autowired
    private GetAllUsersService userService;

    @GetMapping("/user/getAll/users")
    public List<User> getUsers() {
        // Log the incoming request to get all users
        logger.info("Received request to get all users");

        // Retrieve all users using the service
        List<User> users = userService.getAllUsers();

        // Log the successful retrieval of users
        logger.info("Retrieved {} users successfully", users.size());

        // Return the list of users
        return users;
    }
}
