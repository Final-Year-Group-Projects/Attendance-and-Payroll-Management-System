package com.example.PayrollService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PayrollNotificationResponseDTO {
    private String status;
    private String message;
    private Long employeeId;

    public PayrollNotificationResponseDTO(String status, String message, Long employeeId) {
        this.status = status;
        this.message = message;
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}



