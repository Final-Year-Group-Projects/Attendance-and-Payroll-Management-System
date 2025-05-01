// dto/ReimbursementResponseDTO.java
package com.example.PayrollService.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReimbursementResponseDTO {
    private Long id;
    private Long employeeId;
    private String type;
    private Double amount;
    private String description;
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private LocalDate requestDate;
}
