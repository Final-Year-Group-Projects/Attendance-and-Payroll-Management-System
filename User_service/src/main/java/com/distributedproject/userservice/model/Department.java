package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.department.ValidDepartmentHead;
import com.distributedproject.userservice.validation.department.ValidDepartmentName;
import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @ValidDepartmentName
    private String departmentName;
    
    @ValidDepartmentHead
    private String department_head;


    // Getters and setters

    public Long getDepartmentId(){return departmentId;}

    public void setDepartmentId(Long department_id) {
        this.departmentId = department_id;
    }

    public String getDepartmentName(){return departmentName;}

    public void setDepartmentName(String department_name) {
        this.departmentName = department_name;
    }

    public String getDepartmentHead(){return department_head;}

    public void setDepartmentHead(String department_head) {
        this.department_head = department_head;
    }


}
