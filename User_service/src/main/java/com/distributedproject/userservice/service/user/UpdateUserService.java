package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameAlreadyExistsException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import com.distributedproject.userservice.exception.user.UserNotFoundException;  // Import custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(String userId, User userDetails) {
        // Use Optional to find the user by ID
        Optional<User> existingUser = userRepository.findByUserId(String.valueOf(userId));

        // If user is found, update the user details
        if (existingUser.isPresent()) {

            Optional<User> userByName = userRepository.findByUserFullNameIgnoreCase(userDetails.getUserFullName());
            if (userByName.isPresent() && !userByName.get().getUserId().equals(userId)) {
                throw new UserNameAlreadyExistsException(userDetails.getUserFullName());
            }

            User userToUpdate = existingUser.get();
            userToUpdate.setUserFullName(userDetails.getUserFullName());
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
