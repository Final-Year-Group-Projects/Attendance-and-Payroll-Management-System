package com.distributedproject.userservice.service;

import com.distributedproject.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User createUser(User user) {
        user.setId((long) (users.size() + 1)); // Just for testing
        users.add(user);
        return user;
    }
}
