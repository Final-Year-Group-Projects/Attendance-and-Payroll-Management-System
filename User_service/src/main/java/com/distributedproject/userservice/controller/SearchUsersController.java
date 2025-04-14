package com.distributedproject.userservice.controller;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.SearchUserByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SearchUsersController {

    @Autowired
    private SearchUserByNameService userService;

    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }
}
