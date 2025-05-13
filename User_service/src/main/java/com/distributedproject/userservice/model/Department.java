package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.department.ValidDepartmentHead;
import com.distributedproject.userservice.validation.department.ValidDepartmentName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @ValidDepartmentName
    private String departmentName;
    
    @ValidDepartmentHead
    private String department_head;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
