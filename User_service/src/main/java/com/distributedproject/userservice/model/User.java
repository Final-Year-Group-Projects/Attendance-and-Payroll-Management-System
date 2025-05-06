package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.user.ValidUserAddress;
import com.distributedproject.userservice.validation.user.ValidUserName;
import com.distributedproject.userservice.validation.user.ValidUserTelephone;
import com.distributedproject.userservice.validation.user.ValidUserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "userdata")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @ValidUserName
    private String userFullName;

    @ValidUserType
    private String userType;

    @ValidUserAddress
    private String userAddress;

    @ValidUserTelephone
    private String userTelephone;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    // Getters and setters

    public Long getDepartmentId(){return departmentId;}

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getRoleId(){return roleId;}

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userName) {
        this.userFullName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }
}
