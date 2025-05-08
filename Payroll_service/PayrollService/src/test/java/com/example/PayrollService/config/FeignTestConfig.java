package com.example.PayrollService.config;

import com.example.PayrollService.dto.integration.*;
import com.example.PayrollService.feign.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class FeignTestConfig {

    @Bean
    @Primary
    public UserServiceClient userServiceClientMock() {
        return employeeId -> {
            UserDTO dto = new UserDTO();
            dto.setEmployeeId(employeeId);
            dto.setRole("ENGINEER"); // Default test role
            return dto;
        };
    }

    @Bean
    @Primary
    public AttendanceServiceClient attendanceServiceClientMock() {
        return (employeeId, month, year) -> {
            AttendanceDTO dto = new AttendanceDTO();
            dto.setWorkingDays(20);
            dto.setApprovedLeaves(2);
            dto.setNotApprovedLeaves(0);
            dto.setMonth(month);
            dto.setYear(year);
            return dto;
        };
    }
}