package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNotFoundException;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(Long userId) {
        if (userRepository.existsById(String.valueOf(userId))) {
            userRepository.deleteById(String.valueOf(userId));
        } else {
            throw new UserNotFoundException(userId);  // Throw custom exception if user not found
        }
    }
}
