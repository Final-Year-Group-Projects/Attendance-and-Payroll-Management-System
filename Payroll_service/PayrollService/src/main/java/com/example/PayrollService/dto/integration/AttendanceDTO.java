package com.example.PayrollService.dto.integration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1-12")
    @Max(value = 12, message = "Month must be between 1-12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    private Integer year;

    @NotNull(message = "Working days are required")
    @Min(value = 0, message = "Working days cannot be negative")
    private Integer workingDays;

    @NotNull(message = "Approved leaves are required")
    @Min(value = 0, message = "Approved leaves cannot be negative")
    private Integer approvedLeaves;

    @NotNull(message = "Not approved leaves are required")
    @Min(value = 0, message = "Not approved leaves cannot be negative")
    private Integer notApprovedLeaves;
}