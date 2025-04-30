package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);
    boolean existsByDepartmentNameIgnoreCase(String departmentName);

    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName); // 🔍 For uniqueness check
}
