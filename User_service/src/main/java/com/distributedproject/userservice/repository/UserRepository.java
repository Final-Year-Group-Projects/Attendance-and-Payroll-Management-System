package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
