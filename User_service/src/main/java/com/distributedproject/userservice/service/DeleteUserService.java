package com.distributedproject.userservice.service;

import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(String.valueOf(userId))) {
            userRepository.deleteById(String.valueOf(userId));
            return true; // User deleted successfully
        } else {
            return false; // User not found
        }
    }
}
