package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department updateDepartment(Long departmentId, Department departmentDetails) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);

        if (existingDepartment.isPresent()) {
            // Check if new name already exists in another department
            Optional<Department> departmentByName = departmentRepository.findByDepartmentNameIgnoreCase(departmentDetails.getDepartmentName());
            if (departmentByName.isPresent() && !departmentByName.get().getDepartmentId().equals(departmentId)) {
                throw new DepartmentNameAlreadyExistsException(departmentDetails.getDepartmentName());
            }

            Department departmentToUpdate = existingDepartment.get();
            departmentToUpdate.setDepartmentName(departmentDetails.getDepartmentName());
            departmentToUpdate.setDepartmentHead(departmentDetails.getDepartmentHead());

            return departmentRepository.save(departmentToUpdate);
        } else {
            throw new DepartmentNotFoundException(departmentId);
        }
    }
}
