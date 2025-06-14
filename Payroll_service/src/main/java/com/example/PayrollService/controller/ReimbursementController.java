// controller/ReimbursementController.java
package com.example.PayrollService.controller;

import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.service.ReimbursementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/payrolls/reimbursements")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @PostMapping
    public ReimbursementResponseDTO submitRequest(
            @Valid @RequestBody ReimbursementRequestDTO dto,
            HttpServletRequest request) {

        String tokenUserId = (String) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        return reimbursementService.submitRequest(dto,tokenUserId, role);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ReimbursementResponseDTO> getRequestsByEmployee(
            @PathVariable @NotBlank String employeeId) {
            return reimbursementService.getRequestsByEmployee(employeeId);
    }

    @PutMapping("/{reimbursementId}/status")
    public ReimbursementResponseDTO updateStatus(
            @PathVariable @Min(1) Long reimbursementId,
            @RequestParam String status) {
            return reimbursementService.updateStatus(reimbursementId, status);
    }

    @DeleteMapping("/{reimbursementId}")
    public void deleteRequest(
            @PathVariable @Min(1) Long reimbursementId) {
            reimbursementService.deleteRequest(reimbursementId);
    }
}
