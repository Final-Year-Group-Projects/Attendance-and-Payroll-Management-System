package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.SearchUserByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class SearchUsersController {

    private static final Logger logger = LoggerFactory.getLogger(SearchUsersController.class);

    @Autowired
    private SearchUserByNameService userService;

    @GetMapping("/get/users/search")
    public List<User> searchUsers(@RequestParam String name) {
        // Log the incoming request with the search term (user name)
        logger.info("Received request to search users with name: {}", name);

        // Call the service to search users by name
        List<User> users = userService.searchUsersByName(name);

        // Log the result of the search
        logger.info("Found {} users matching the name: {}", users.size(), name);

        // Return the list of matching users
        return users;
    }
}
