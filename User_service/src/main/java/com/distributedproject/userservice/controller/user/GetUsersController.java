package com.distributedproject.userservice.controller.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.GetAllUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetUsersController {

    @Autowired
    private GetAllUsersService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
