package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.role.ValidRoleName;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ValidRoleName
    private String roleName;

    private String roleDescription;


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


}
