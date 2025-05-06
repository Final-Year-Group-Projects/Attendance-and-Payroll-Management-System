package com.example.PayrollService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRequestDTO {
    private String employeeId;
    private Double basicSalary;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
    private Double deductions;
    private String role;
}
