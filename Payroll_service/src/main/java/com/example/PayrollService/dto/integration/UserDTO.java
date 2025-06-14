package com.example.PayrollService.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty("userId")
    private String employeeId;

    @JsonProperty("userType")
    private String role;
}
