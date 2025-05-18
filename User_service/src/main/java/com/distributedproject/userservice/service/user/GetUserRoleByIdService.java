package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserRoleNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserRoleByIdService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Optional<String> getUserRoleById(String userId) {
        // Fetch the user by userId
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the roleId is null
            if (user.getRoleId() == null) {
                throw new UserRoleNotFoundException(userId);  // Throw exception if roleId is null
            }

            // Fetch the role by roleId (Long to String conversion can be handled)
            return roleRepository.findById(user.getRoleId())
                    .map(role -> role.getRoleName());  // Only return roleName
        }

        return Optional.empty();  // Return empty if the user is not found
    }
}
