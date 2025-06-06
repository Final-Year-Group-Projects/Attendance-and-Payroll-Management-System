package com.distributedproject.authservice.repository;

import com.distributedproject.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(String userId);
    Optional<User> findByUsernameAndUserId(String username, String userId);

}
