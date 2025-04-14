package com.distributedproject.userservice.service;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create and save a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Retrieve all users from the database
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
