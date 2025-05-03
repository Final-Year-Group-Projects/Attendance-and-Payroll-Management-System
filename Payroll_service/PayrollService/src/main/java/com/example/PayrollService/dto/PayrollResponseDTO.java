package com.example.PayrollService.dto;

import com.example.PayrollService.entity.PayrollRecord;
import lombok.Data;


import java.time.LocalDate;

@Data
public class PayrollResponseDTO {
    private Long id;
    private String employeeId;
    private Double basicSalary;
    private Double netSalary;
    private LocalDate generatedDate;
    private String status;

    public PayrollResponseDTO() {}

    public PayrollResponseDTO(
            Long id, String employeeId, Double basicSalary,Double netSalary, LocalDate generatedDate, String status
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.netSalary = netSalary;
        this.generatedDate = generatedDate;
        this.status = status;
    }

    public static PayrollResponseDTO fromRecord(PayrollRecord record) {
        return new PayrollResponseDTO(
                record.getId(),
                record.getEmployeeId(),
                record.getBasicSalary(),
                record.getNetSalary(),
                record.getGeneratedDate(),
                record.getStatus()
        );
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }
}
