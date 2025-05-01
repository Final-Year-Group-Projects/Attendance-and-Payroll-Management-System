package com.example.PayrollService.dto;

import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.exception.ResourceNotFoundException;
import com.example.PayrollService.repository.PayrollRepository;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponseDTO {
    private Long id;
    private Long employeeId;
    private Double netSalary;
    private LocalDate generatedDate;
    private String status;

    public static PayrollResponseDTO fromRecord(PayrollRecord record) {
        return PayrollResponseDTO.builder()
                .id(record.getId())
                .employeeId(record.getEmployeeId())
                .netSalary(record.getNetSalary())
                .generatedDate(record.getGeneratedDate())
                .status(record.getStatus())
                .build();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
