// dto/ReimbursementRequestDTO.java
package com.example.PayrollService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReimbursementRequestDTO {
    private Long employeeId;
    private String type; // e.g., "travel", "medical"
    private Double amount;
    private String description;
}
