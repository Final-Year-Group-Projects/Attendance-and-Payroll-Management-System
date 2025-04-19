package com.example.PayrollService.controller;

import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payrolls")
public class PayrollNotifyController {

    @Autowired
    private PayrollRepository payrollRepository;

    @PostMapping("/{employeeId}/notify")
    public ResponseEntity<?> notifyUserService(@PathVariable Long employeeId) {
        List<PayrollRecord> payrolls = payrollRepository.findByEmployeeId(employeeId);

        if (payrolls.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "No payroll records found for employee ID: " + employeeId
            ));
        }

        PayrollRecord latest = payrolls.get(payrolls.size() - 1); // most recent payroll

        String simulatedMessage = String.format(
                "Notification simulated: Sent payroll (ID: %d, Net Salary: %.2f, Date: %s) for employee ID: %d",
                latest.getId(),
                latest.getNetSalary(),
                latest.getGeneratedDate(),
                employeeId
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", simulatedMessage);
        response.put("employeeId", employeeId);

        return ResponseEntity.ok(response);
    }

}
