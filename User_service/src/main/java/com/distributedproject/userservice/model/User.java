package com.distributedproject.userservice.model;

import com.distributedproject.userservice.validation.ValidUserAddress;
import com.distributedproject.userservice.validation.ValidUserName;
import com.distributedproject.userservice.validation.ValidUserTelephone;
import com.distributedproject.userservice.validation.ValidUserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    // Getters and setters

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
