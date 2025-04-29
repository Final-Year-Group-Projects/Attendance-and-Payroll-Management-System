package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNameNotFoundException;
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
        List<User> users = userRepository.findByUserFullNameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new UserNameNotFoundException(name);
        }
        return users;
    }
}
