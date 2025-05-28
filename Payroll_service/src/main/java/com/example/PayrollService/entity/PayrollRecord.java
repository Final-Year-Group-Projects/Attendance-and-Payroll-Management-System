package com.example.PayrollService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payroll_records")
public class PayrollRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private Double basicSalary;
    private Double medicalAllowance;
    private Double otherAllowance;
    private Double taxDeduction;
    private Double transportFee;
    private Double sportsFee;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;
    private Double noPay;
    private Double netSalary;
    private LocalDate generatedDate;
    private LocalDateTime updatedDate;
    private Integer month;
    private Integer year;
    private String status;

}
