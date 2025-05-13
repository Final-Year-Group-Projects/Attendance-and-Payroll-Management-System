package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.UserDepartmentNotFoundException;
import com.distributedproject.userservice.exception.user.UserRoleNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserDepartmentByIdService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Optional<String> getUserDepartmentById(String userId) {
        // Fetch the user by userId
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the roleId is null
            if (user.getDepartmentId() == null) {
                throw new UserDepartmentNotFoundException(userId);  // Throw exception if roleId is null
            }

            // Fetch the role by roleId (Long to String conversion can be handled)
            return departmentRepository.findById(user.getDepartmentId())
                    .map(department -> department.getDepartmentName());  // Only return roleName
        }

        return Optional.empty();  // Return empty if the user is not found
    }
}
