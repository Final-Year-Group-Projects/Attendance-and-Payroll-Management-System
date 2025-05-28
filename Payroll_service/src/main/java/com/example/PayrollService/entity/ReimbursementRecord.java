// entity/ReimbursementRecord.java
package com.example.PayrollService.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reimbursement_record")
public class ReimbursementRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private String type;
    private Double amount;
    private String description;
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private LocalDate requestDate;
}
