package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollNotificationResponseDTO;
import com.example.PayrollService.service.PayrollService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollNotifyController {

    @Autowired
    private PayrollService payrollService;

    @PostMapping("/{employeeId}/notify")
    public ResponseEntity<PayrollNotificationResponseDTO> notifyEmployee(
            @PathVariable @NotBlank String employeeId
    ) {
        PayrollNotificationResponseDTO response = payrollService.generatePayrollNotification(employeeId);
        return response.getStatus().equals("error")
                ? ResponseEntity.status(404).body(response)
                : ResponseEntity.ok(response);
    }
}
