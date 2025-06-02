package com.distributedproject.authservice.model;

import com.distributedproject.authservice.validation.ValidRole;
import jakarta.persistence.*;

@Entity
@Table(name = "userdata")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    @ValidUserId
    private String userId;

    @Column(unique = true)
    private String username;

    private String password;

    @ValidRole
    private String role;

    public Long getId(){return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword(){return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername(){return username;}

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole(){return role;}

    public void setRole(String role) {
        this.role = role;
    }

}
