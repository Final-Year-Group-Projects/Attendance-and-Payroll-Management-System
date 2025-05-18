package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserIdAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameAlreadyExistsException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public User createUser(User user) {
        // Check if the user name already exists
        if (userRepository.existsByUserFullNameIgnoreCase(user.getUserFullName())) {
            throw new UserNameAlreadyExistsException(user.getUserFullName());
        }

        // Check if the user ID already exists
        if (userRepository.existsByUserIdIgnoreCase(user.getUserId())) {
            throw new UserIdAlreadyExistsException(user.getUserId());
        }

        // Check if the role exists
        boolean roleExists = roleRepository.existsById(user.getRoleId());
        if (!roleExists) {
            throw new RoleNotFoundException(user.getRoleId());
        }

        // Check if the department exists
        boolean departmentExists = departmentRepository.existsById(user.getDepartmentId());
        if (!departmentExists) {
            throw new DepartmentNotFoundException(user.getDepartmentId());
        }

        // Save the user to the repository
        return userRepository.save(user);
    }
}
