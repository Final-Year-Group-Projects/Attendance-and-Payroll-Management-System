package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByRoleNameContainingIgnoreCase(String name);
    boolean existsByRoleNameIgnoreCase(String name);
}
