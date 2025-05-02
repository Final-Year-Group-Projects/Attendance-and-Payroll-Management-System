package com.example.PayrollService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PayrollNotificationResponseDTO {
    private String status;
    private String message;
    private String employeeId;

    public PayrollNotificationResponseDTO(String status, String message, String employeeId) {
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}



