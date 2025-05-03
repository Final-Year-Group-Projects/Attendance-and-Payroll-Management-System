package com.example.PayrollService.controller;

import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollDeleteController {
    @Autowired
    private PayrollService payrollService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayroll(@PathVariable Long id) {
        boolean isDeleted = payrollService.deletePayroll(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body("No payroll record found for user " + id);
        }
        return ResponseEntity.ok("Payroll deleted for user " + id);
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<?> deletePayrollsByEmployeeId(@PathVariable String employeeId) {
        boolean deleted = payrollService.deletePayrollsByEmployeeId(employeeId);
        if (!deleted) {
            return ResponseEntity.status(404).body("No payroll records found for employee " + employeeId);
        }
        return ResponseEntity.ok("Deleted payroll records for employee " + employeeId);
    }

}
