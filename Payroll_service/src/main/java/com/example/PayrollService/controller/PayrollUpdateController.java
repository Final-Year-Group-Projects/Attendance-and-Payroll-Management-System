package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollUpdateController {
    @Autowired
    private PayrollService payrollService;

    @PutMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> updatePayroll(
            @PathVariable @Min(1)Long id,
            @Valid @RequestBody PayrollRequestDTO requestDTO) {
        PayrollResponseDTO response = payrollService.updatePayroll(id, requestDTO);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{payrollId}/status")
    public ResponseEntity<PayrollResponseDTO> updateStatus(
            @PathVariable @Min(1) Long payrollId,
            @RequestParam String status) {
        PayrollResponseDTO updatedPayroll = payrollService.updatePayrollStatus(payrollId, status);
        return ResponseEntity.ok(updatedPayroll);
    }
}
