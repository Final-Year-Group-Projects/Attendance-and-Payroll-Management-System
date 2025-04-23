package com.example.PayrollService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payroll_service_db")
public class PayrollRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Double basicSalary;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
    private Double deductions;
    private Double netSalary;
    private LocalDate generatedDate;
    private Integer month;
    private Integer year;
    private String status;


    // Getters
    public Long getId() {
        return id;
    }

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

    public Double getNetSalary() {
        return netSalary;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
