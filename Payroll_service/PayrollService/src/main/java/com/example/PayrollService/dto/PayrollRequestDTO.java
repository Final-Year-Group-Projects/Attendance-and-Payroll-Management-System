package com.example.PayrollService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRequestDTO {
    private Long employeeId;
    private Double basicSalary;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
    private Double deductions;
    private String role;

    // Getters
    public Long getEmployeeId() {
        return employeeId;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public Integer getWorkingDays() {
        return workingDays;
    }

    public Integer getApprovedLeaves() {
        return approvedLeaves;
    }

    public Integer getNotApprovedLeaves() {
        return notApprovedLeaves;
    }

    public Double getDeductions() {
        return deductions;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }


    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    public void setApprovedLeaves(Integer approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public void setNotApprovedLeaves(Integer notApprovedLeaves) {
        this.notApprovedLeaves = notApprovedLeaves;
    }

    public void setDeductions(Double deductions) {
        this.deductions = deductions;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
