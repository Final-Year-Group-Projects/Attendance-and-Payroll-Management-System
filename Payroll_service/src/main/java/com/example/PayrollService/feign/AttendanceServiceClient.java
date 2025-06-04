package com.example.PayrollService.feign;

import com.example.PayrollService.config.FeignConfig;
import com.example.PayrollService.dto.integration.AttendanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "attendance-service", url = "${gateway.url}", configuration = FeignConfig.class
)
public interface AttendanceServiceClient {
    @GetMapping("/attendance/{employeeId}/details")
    AttendanceDTO getAttendanceDetails(
            @PathVariable("employeeId") String employeeId,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year);
}
