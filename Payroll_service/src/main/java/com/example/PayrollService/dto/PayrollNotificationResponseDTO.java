package com.example.PayrollService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollNotificationResponseDTO {
    private String status;
    private String message;
    private String employeeId;
}



