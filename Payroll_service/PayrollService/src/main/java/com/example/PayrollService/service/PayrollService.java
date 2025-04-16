package com.example.PayrollService.service;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;

public interface PayrollService {
    PayrollResponseDTO createPayroll(PayrollRequestDTO requestDTO);
}