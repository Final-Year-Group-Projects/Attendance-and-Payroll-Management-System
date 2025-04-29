package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByUserFullNameIgnoreCase(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User full name already taken.");
        }
        return userRepository.save(user);
    }
}
