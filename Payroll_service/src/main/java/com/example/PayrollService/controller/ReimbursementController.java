// controller/ReimbursementController.java
package com.example.PayrollService.controller;

import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.service.ReimbursementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reimbursements")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @PostMapping
    public ReimbursementResponseDTO submitRequest(
            @Valid @RequestBody ReimbursementRequestDTO dto) {
            return reimbursementService.submitRequest(dto);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ReimbursementResponseDTO> getRequestsByEmployee(
            @PathVariable @NotBlank String employeeId) {
            return reimbursementService.getRequestsByEmployee(employeeId);
    }

    @PutMapping("/{reimbursementId}/status")
    public ReimbursementResponseDTO updateStatus(
            @PathVariable @Min(1) Long reimbursementId,
            @RequestParam @Pattern(regexp = "PENDING|APPROVED|REJECTED") String status) {
            return reimbursementService.updateStatus(reimbursementId, status);
    }

    @DeleteMapping("/{reimbursementId}")
    public void deleteRequest(
            @PathVariable @Min(1) Long reimbursementId) {
            reimbursementService.deleteRequest(reimbursementId);
    }
}
