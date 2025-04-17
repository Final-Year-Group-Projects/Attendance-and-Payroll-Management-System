package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import com.distributedproject.userservice.exception.user.UserNotFoundException;  // Import custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(Long userId, User userDetails) {
        // Use Optional to find the user by ID
        Optional<User> existingUser = userRepository.findById(String.valueOf(userId));

        // If user is found, update the user details
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setUserName(userDetails.getUserName());
            userToUpdate.setUserAddress(userDetails.getUserAddress());
            userToUpdate.setUserTelephone(userDetails.getUserTelephone());
            userToUpdate.setUserType(userDetails.getUserType());

            // Save the updated user to the repository
            return userRepository.save(userToUpdate);
        } else {
            // If user is not found, throw custom exception
            throw new UserNotFoundException(userId);  // Use custom exception here
        }
    }
}
