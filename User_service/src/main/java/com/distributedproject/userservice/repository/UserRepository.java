package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserFullNameContainingIgnoreCase(String name);
    boolean existsByUserFullNameIgnoreCase(String userFullName);
    Optional<User> findByUserFullNameIgnoreCase(String userFullName);

    Optional<User> findByUserId(Long userId);  // Add this
}
