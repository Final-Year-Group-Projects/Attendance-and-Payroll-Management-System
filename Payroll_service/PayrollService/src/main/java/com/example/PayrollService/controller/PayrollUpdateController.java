package com.example.PayrollService.controller;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollUpdateController {
    @Autowired
    private PayrollService payrollService;

    @PutMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> updatePayroll(@PathVariable Long id, @RequestBody PayrollRequestDTO requestDTO) {
        PayrollResponseDTO response = payrollService.updatePayroll(id, requestDTO);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
