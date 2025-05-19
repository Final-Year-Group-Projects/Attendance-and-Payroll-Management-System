// service/ReimbursementServiceImpl.java
package com.example.PayrollService.service;

import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.exception.ResourceNotFoundException;
import com.example.PayrollService.exception.ValidationException;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    // Add constants for validation rules
    private static final List<String> VALID_TYPES = List.of("TRAVEL", "MEDICAL", "MEAL", "OTHER");
    private static final List<String> VALID_STATUSES = List.of("PENDING", "APPROVED", "REJECTED");

    private ReimbursementResponseDTO toDTO(ReimbursementRecord record) {
        return ReimbursementResponseDTO.builder()
                .id(record.getId())
                .employeeId(record.getEmployeeId())
                .type(record.getType())
                .amount(record.getAmount())
                .description(record.getDescription())
                .status(record.getStatus())
                .requestDate(record.getRequestDate())
                .build();
    }

    private void validateEmployeeExists(String employeeId) {
        if (!reimbursementRepository.existsByEmployeeId(employeeId)) {
            throw new ResourceNotFoundException("Employee not found: " + employeeId);
        }
    }

    private void validateReimbursementExists(Long id) {
        if (!reimbursementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reimbursement not found with ID: " + id);
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals("APPROVED") && newStatus.equalsIgnoreCase("PENDING")) {
            throw new ValidationException("Cannot revert APPROVED request to PENDING");
        }
        if (currentStatus.equals("REJECTED") && !newStatus.equalsIgnoreCase("PENDING")) {
            throw new ValidationException("REJECTED requests can only be reset to PENDING");
        }
    }

    private void validateReimbursementType(String type) {
        ValidationUtils.validateInList(type, VALID_TYPES, "reimbursement type");
    }

    private void validateAgainstBusinessPolicies(Double amount, String type) {
        if ("TRAVEL".equals(type) && amount > 6000.00) {
            throw new ValidationException("Travel reimbursements cannot exceed RS.6000");
        }
        if ("MEDICAL".equals(type) && amount > 10000.00) {
            throw new ValidationException("Medical reimbursements cannot exceed Rs.10000");
        }
        if ("MEAL".equals(type) && amount > 4000.00) {
            throw new ValidationException("Meal reimbursements cannot exceed Rs.4000");
        }
        if ("OTHER".equals(type) && amount > 3000.00) {
            throw new ValidationException("Other reimbursements cannot exceed Rs.3000");
        }
    }

    @Override
    public ReimbursementResponseDTO submitRequest(ReimbursementRequestDTO dto) {
        validateReimbursementType(dto.getType());
        validateAgainstBusinessPolicies(dto.getAmount(), dto.getType().toUpperCase());

        ReimbursementRecord record = new ReimbursementRecord();
        record.setEmployeeId(dto.getEmployeeId());
        record.setType(dto.getType());
        record.setAmount(dto.getAmount());
        record.setDescription(dto.getDescription());
        record.setStatus("PENDING");
        record.setRequestDate(LocalDate.now());
        record = reimbursementRepository.save(record);
        return toDTO(reimbursementRepository.save(record));
    }

    @Override
    public List<ReimbursementResponseDTO> getRequestsByEmployee(String employeeId) {
        validateEmployeeExists(employeeId);
        return reimbursementRepository.findByEmployeeId(employeeId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ReimbursementResponseDTO updateStatus(Long id, String status) {
        validateReimbursementExists(id);

        ValidationUtils.validateInList(status, VALID_STATUSES, "status");

        ReimbursementRecord record = reimbursementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reimbursement not found"));

        validateStatusTransition(record.getStatus(), status);

        // Validate status transition
        if (record.getStatus().equals("APPROVED") && status.equalsIgnoreCase("PENDING")) {
            throw new ValidationException("Cannot revert APPROVED request to PENDING");
        }
        record.setStatus(status);
        reimbursementRepository.save(record);
        return toDTO(reimbursementRepository.save(record));
    }

    @Override
    public void deleteRequest(Long id) {
        validateReimbursementExists(id);
        reimbursementRepository.deleteById(id);
    }

}
