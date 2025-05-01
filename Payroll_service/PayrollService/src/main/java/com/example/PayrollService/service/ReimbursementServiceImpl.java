// service/ReimbursementServiceImpl.java
package com.example.PayrollService.service;

import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.repository.ReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Override
    public ReimbursementResponseDTO submitRequest(ReimbursementRequestDTO dto) {
        ReimbursementRecord record = new ReimbursementRecord();
        record.setEmployeeId(dto.getEmployeeId());
        record.setType(dto.getType());
        record.setAmount(dto.getAmount());
        record.setDescription(dto.getDescription());
        record.setStatus("PENDING");
        record.setRequestDate(LocalDate.now());
        record = reimbursementRepository.save(record);
        return toDTO(record);
    }

    @Override
    public List<ReimbursementResponseDTO> getRequestsByEmployee(Long employeeId) {
        return reimbursementRepository.findByEmployeeId(employeeId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ReimbursementResponseDTO updateStatus(Long id, String status) {
        ReimbursementRecord record = reimbursementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reimbursement not found"));
        record.setStatus(status);
        reimbursementRepository.save(record);
        return toDTO(record);
    }

    @Override
    public void deleteRequest(Long id) {
        reimbursementRepository.deleteById(id);
    }

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
}
