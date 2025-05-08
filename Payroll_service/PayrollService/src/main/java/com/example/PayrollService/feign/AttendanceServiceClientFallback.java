package com.example.PayrollService.feign;

import com.example.PayrollService.dto.integration.AttendanceDTO;
import org.springframework.stereotype.Component;

@Component
public class AttendanceServiceClientFallback implements AttendanceServiceClient {
    @Override
    public AttendanceDTO getAttendanceDetails(String employeeId, Integer month, Integer year) {
        // Return fallback data
        return new AttendanceDTO(employeeId, month, year, 18, 2, 0);
    }
}