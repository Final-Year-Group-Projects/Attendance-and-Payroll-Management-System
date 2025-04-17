package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollReadController {
    @Autowired
    private PayrollService payrollService;

    @GetMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> getPayrollById(@PathVariable Long id) {
        PayrollResponseDTO payrollResponse = payrollService.getPayrollById(id);
        if (payrollResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payrollResponse);
    }
}

