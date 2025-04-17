package com.distributedproject.userservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long department_id;

    private String departmentName;

    private String department_head;


    // Getters and setters

    public Long getDepartmentId(){return department_id;}

    public void setDepartmentId(Long department_id) {
        this.department_id = department_id;
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
