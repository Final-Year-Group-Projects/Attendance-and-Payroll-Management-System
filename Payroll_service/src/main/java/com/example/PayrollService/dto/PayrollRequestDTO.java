package com.example.PayrollService.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRequestDTO {
    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1-12")
    @Max(value = 12, message = "Month must be between 1-12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    private Integer year;

    @Min(value = 0, message = "Working days cannot be negative")
    private Integer workingDays;

    @Min(value = 0, message = "Approved leaves cannot be negative")
    private Integer approvedLeaves;

    @Min(value = 0, message = "Not approved leaves cannot be negative")
    private Integer notApprovedLeaves;
    
    private String role;
}
