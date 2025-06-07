package com.distributedproject.userservice.model;

public class UserDTO {
    private String employeeId;
    private String role;

    public UserDTO() {
    }

    public UserDTO(String employeeId, String role) {
        this.employeeId = employeeId;
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
