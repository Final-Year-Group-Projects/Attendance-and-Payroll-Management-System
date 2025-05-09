// dto/ReimbursementRequestDTO.java
package com.example.PayrollService.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReimbursementRequestDTO {
    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

    @NotBlank(message = "Type is required (e.g., TRAVEL, MEDICAL)")
    @Pattern(regexp = "^(TRAVEL|MEDICAL|MEAL|OTHER)$",
            message = "Type must be TRAVEL, MEDICAL, MEAL, or OTHER")
    private String type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Max(value = 5000, message = "Amount cannot exceed 5000.00")
    private Double amount;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;
}
