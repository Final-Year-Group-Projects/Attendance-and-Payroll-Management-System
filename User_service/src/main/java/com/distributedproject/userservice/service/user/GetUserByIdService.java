package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByIdService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(String.valueOf(userId));
    }
}
