package com.distributedproject.userservice.repository;

import com.distributedproject.userservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);
    boolean existsByDepartmentNameIgnoreCase(String departmentName);
}
