package com.distributedproject.userservice.service;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(Long userId, User userDetails) {
        Optional<User> existingUser = userRepository.findById(String.valueOf(userId));

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setUserName(userDetails.getUserName());
            userToUpdate.setUserAddress(userDetails.getUserAddress());
            userToUpdate.setUserTelephone(userDetails.getUserTelephone());
            userToUpdate.setUserType(userDetails.getUserType());

            return userRepository.save(userToUpdate);
        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }
}
