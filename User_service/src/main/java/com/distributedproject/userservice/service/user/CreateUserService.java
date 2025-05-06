package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public User createUser(User user) {
        if (userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User full name already taken.");
        }
        boolean roleExists = roleRepository.existsById(String.valueOf(user.getRoleId()));
        boolean departmentExists = departmentRepository.existsById(String.valueOf(user.getDepartmentId()));

        // Collect all missing references
        StringBuilder errorMessage = new StringBuilder();

        if (!roleExists) {
            errorMessage.append("Role ID ").append(user.getRoleId()).append(" does not exist. ");
        }
        if (!departmentExists) {
            errorMessage.append("Department ID ").append(user.getDepartmentId()).append(" does not exist.");
        }

        if (errorMessage.length() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString().trim());
        }
        return userRepository.save(user);
    }
}
