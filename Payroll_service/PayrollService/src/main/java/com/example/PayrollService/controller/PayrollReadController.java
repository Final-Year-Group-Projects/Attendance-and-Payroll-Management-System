package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollResponseDTO>> getPayrollsByEmployeeId(@PathVariable String employeeId) {
        List<PayrollResponseDTO> payrolls = payrollService.getPayrollsByEmployeeId(employeeId);
        if (payrolls.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PayrollResponseDTO>> getAllPayrolls() {
        List<PayrollResponseDTO> allPayrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(allPayrolls);
    }


}

