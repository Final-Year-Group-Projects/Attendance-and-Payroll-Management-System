package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department updateDepartment(Long departmentId, Department departmentDetails) {
        // Use Optional to find the user by ID
        Optional<Department> existingDepartment = departmentRepository.findById(String.valueOf(departmentId));

        // If user is found, update the user details
        if (existingDepartment.isPresent()) {
            Department departmentToUpdate = existingDepartment.get();
            departmentToUpdate.setDepartmentName(departmentDetails.getDepartmentName());
            departmentToUpdate.setDepartmentHead(departmentDetails.getDepartmentHead());

            // Save the updated user to the repository
            return departmentRepository.save(departmentToUpdate);
        } else {
            // If user is not found, throw custom exception
            throw new DepartmentNotFoundException(departmentId);  // Use custom exception here
        }
    }
}
