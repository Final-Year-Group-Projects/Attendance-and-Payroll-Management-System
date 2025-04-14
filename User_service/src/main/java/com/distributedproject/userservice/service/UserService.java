package com.distributedproject.userservice.service;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(String.valueOf(userId));
    }

    public User updateUser(Long userId, User userDetails) {
        Optional<User> existingUser = userRepository.findById(String.valueOf(userId));

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setUserName(userDetails.getUserName());
            userToUpdate.setUserAddress(userDetails.getUserAddress());
            userToUpdate.setUserTelephone(userDetails.getUserTelephone());
            userToUpdate.setUserType(userDetails.getUserType());

            return userRepository.save(userToUpdate); // Save the updated user
        } else {
            throw new RuntimeException("User not found with id " + userId); // Handle case when user not found
        }
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(String.valueOf(userId))) {
            userRepository.deleteById(String.valueOf(userId));
            return true; // User deleted successfully
        } else {
            return false; // User not found
        }
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByUserNameContainingIgnoreCase(name);
    }
}
