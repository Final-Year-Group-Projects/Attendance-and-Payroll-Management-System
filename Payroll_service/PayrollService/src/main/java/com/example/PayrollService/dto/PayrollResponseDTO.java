package com.example.PayrollService.dto;

import com.example.PayrollService.entity.PayrollRecord;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollResponseDTO {
    private Long id;
    private String employeeId;
    private Double basicSalary;
    private Double netSalary;
    private LocalDate generatedDate;
    private String status;

    public static PayrollResponseDTO fromRecord(PayrollRecord record) {
        return new PayrollResponseDTO(
                record.getId(),
                record.getEmployeeId(),
                record.getBasicSalary(),
                record.getNetSalary(),
                record.getGeneratedDate(),
                record.getStatus()
        );
    }
}
