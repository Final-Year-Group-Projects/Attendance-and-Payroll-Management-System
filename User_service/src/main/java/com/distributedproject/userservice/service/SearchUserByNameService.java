package com.distributedproject.userservice.service;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUserByNameService {

    @Autowired
    private UserRepository userRepository;

    public List<User> searchUsersByName(String name) {
        return userRepository.findByUserNameContainingIgnoreCase(name);
    }
}
