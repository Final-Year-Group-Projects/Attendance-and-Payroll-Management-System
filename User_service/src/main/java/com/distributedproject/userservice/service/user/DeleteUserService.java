package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(Long userId) {
        // Find user by the custom user_id field
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            // Delete using the internal primary key (id)
            userRepository.delete(userOptional.get());
        } else {
            throw new UserNotFoundException(userId);
        }
    }
}
