package com.example.PayrollService.dto.integration;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private String employeeId;
    private Integer month;
    private Integer year;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
}