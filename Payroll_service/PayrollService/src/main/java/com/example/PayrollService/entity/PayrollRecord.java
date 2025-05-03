package com.example.PayrollService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payroll_records")
public class PayrollRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private Double basicSalary;
    private Double medicalAllowance;
    private Double otherAllowance;
    private Double taxDeduction;
    private Double transportFee;
    private Double sportsFee;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
    private Double noPay;
    private Double netSalary;
    private LocalDate generatedDate;
    private LocalDateTime updatedDate;
    private Integer month;
    private Integer year;
    private String status;


    // Getters
    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
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

    public Double getNetSalary() {
        return netSalary;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public Double getMedicalAllowance() {
        return medicalAllowance;
    }

    public Double getTransportFee() {
        return transportFee;
    }

    public Double getOtherAllowance() {
        return otherAllowance;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    public Double getSportsFee() {
        return sportsFee;
    }

    public Double getNoPay() {
        return noPay;
    }


    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
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

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setUpdateDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
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

    public void setMedicalAllowance(Double medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public void setTransportAllowance(Double transportFee) {
        this.transportFee = transportFee;
    }

    public void setOtherAllowance(Double otherAllowance) {
        this.otherAllowance = otherAllowance;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public void setSportsFee(Double sportsFee) {
        this.sportsFee = sportsFee;
    }

    public void setNoPay(Double noPay) {
        this.noPay = noPay;
    }

}
