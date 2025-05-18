package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {  // Change String to Long
    List<Role> findByRoleNameContainingIgnoreCase(String name);
    boolean existsByRoleNameIgnoreCase(String name);
    Optional<Role> findByRoleNameIgnoreCase(String name);
}
