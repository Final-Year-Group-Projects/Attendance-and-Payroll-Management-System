// service/ReimbursementService.java
package com.example.PayrollService.service;

import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import java.util.List;

public interface ReimbursementService {
    ReimbursementResponseDTO submitRequest(ReimbursementRequestDTO dto, String tokenUserId, String role);
    List<ReimbursementResponseDTO> getRequestsByEmployee(String employeeId);
    ReimbursementResponseDTO updateStatus(Long id, String status);
    void deleteRequest(Long id);
}
