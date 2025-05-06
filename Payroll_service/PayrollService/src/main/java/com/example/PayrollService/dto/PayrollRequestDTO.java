package com.example.PayrollService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRequestDTO {
    private String employeeId;
    private Integer month;
    private Integer year;
}
