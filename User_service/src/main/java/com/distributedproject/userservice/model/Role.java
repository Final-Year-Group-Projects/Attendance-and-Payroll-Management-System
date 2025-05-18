package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.role.ValidRoleDescription;
import com.distributedproject.userservice.validation.role.ValidRoleId;
import com.distributedproject.userservice.validation.role.ValidRoleName;
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
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ValidRoleName
    private String roleName;

    @ValidRoleDescription
    private String roleDescription;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Getters and setters

    public Long getRoleId(){return roleId;}

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName(){return roleName;}

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription(){return roleDescription;}

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
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
