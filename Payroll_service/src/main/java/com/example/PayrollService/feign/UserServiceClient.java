package com.example.PayrollService.feign;

import com.example.PayrollService.config.FeignConfig;
import com.example.PayrollService.dto.integration.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://api-gateway",  configuration = FeignConfig.class
)
public interface UserServiceClient {

    // Assuming the endpoint returns { "employeeId": "...", "role": "..." }
    @GetMapping("/user/{employeeId}/details")
    UserDTO getUserDetails(@PathVariable("employeeId") String employeeId);
}
