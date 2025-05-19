package com.example.PayrollService.service;

import com.example.PayrollService.dto.PayrollNotificationResponseDTO;
import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;

import java.util.List;

public interface PayrollService {
    PayrollResponseDTO createPayroll(PayrollRequestDTO requestDTO);
    PayrollResponseDTO getPayrollById(Long id);
    PayrollResponseDTO updatePayroll(Long id, PayrollRequestDTO dto);
    boolean deletePayroll(Long id);
    List<PayrollResponseDTO> getPayrollsByEmployeeId(String employeeId);
    void generatePayrollsForAllEmployees(Integer month, Integer year);
    List<PayrollResponseDTO> getAllPayrolls();
    PayrollNotificationResponseDTO generatePayrollNotification(String employeeId);
    PayrollResponseDTO updatePayrollStatus(Long id, String status);
    boolean deletePayrollsByEmployeeId(String employeeId);




}