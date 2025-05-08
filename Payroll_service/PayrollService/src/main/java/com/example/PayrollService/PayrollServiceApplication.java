package com.example.PayrollService;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@EnableFeignClients(basePackages = "com.example.PayrollService.feign")
@SpringBootApplication
public class PayrollServiceApplication {

	public static void main(String[] args) {SpringApplication.run(PayrollServiceApplication.class, args);
	}

}

