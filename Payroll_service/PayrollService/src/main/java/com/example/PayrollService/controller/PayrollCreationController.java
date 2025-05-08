package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollCreationController {
    @Autowired
    private PayrollService payrollService;

    @PostMapping
    public ResponseEntity<PayrollResponseDTO> createPayroll(
            @Valid @RequestBody PayrollRequestDTO requestDTO) {
            PayrollResponseDTO response = payrollService.createPayroll(requestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/generateAll")
    public ResponseEntity<String> generatePayrollsForAll(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        // Validate month/year if provided
        if (month != null && (month < 1 || month > 12)) {
            throw new IllegalArgumentException("Month must be 1-12");
        }
        if (year != null && year < 2000) {
            throw new IllegalArgumentException("Year must be >= 2025");
        }

        payrollService.generatePayrollsForAllEmployees(month, year);
        return ResponseEntity.ok("Payrolls generated for all employees.");
    }
}
