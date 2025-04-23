package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollCreationController {
    @Autowired
    private PayrollService payrollService;

    //For testing application
//    @GetMapping("/")
//    public String home() {
//        return "Payroll Service is running!";
//    }

    @PostMapping
    public ResponseEntity<PayrollResponseDTO> createPayroll(@RequestBody PayrollRequestDTO requestDTO) {
        PayrollResponseDTO response = payrollService.createPayroll(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/generateAll")
    public ResponseEntity<String> generatePayrollsForAll(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        payrollService.generatePayrollsForAllEmployees(month, year);
        return ResponseEntity.ok("Payrolls generated for all employees.");
    }
}
