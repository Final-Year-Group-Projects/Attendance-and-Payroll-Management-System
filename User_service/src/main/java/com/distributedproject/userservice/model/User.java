package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.user.ValidUserAddress;
import com.distributedproject.userservice.validation.user.ValidUserName;
import com.distributedproject.userservice.validation.user.ValidUserTelephone;
import com.distributedproject.userservice.validation.user.ValidUserType;
import jakarta.persistence.*;

@Entity
@Table(name = "userdata")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ValidUserName
    private String userName;

    @ValidUserType
    private String userType;

    @ValidUserAddress
    private String userAddress;

    @ValidUserTelephone
    private String userTelephone;

    private Long departmentId;

    // Getters and setters

    public Long getDepartmentId(){return departmentId;}

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
