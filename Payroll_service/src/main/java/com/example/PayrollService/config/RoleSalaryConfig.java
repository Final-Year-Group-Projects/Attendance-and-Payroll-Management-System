package com.example.PayrollService.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleSalaryConfig {
    ENGINEER(50000.0, 2000.0, 1000.0,1000.0, 500.0),
    MANAGER(70000.0, 3000.0, 1000.0,2000.0, 500.0),
    HR(40000.0, 1500.0, 1000.0,800.0, 500.0),
    EMPLOYEE(40000.0, 1500.0, 1000.0,800.0, 500.0),
    ADMIN(40000.0, 1500.0, 1000.0,800.0, 500.0);


    public final double basicSalary;
    public final double medicalAllowance;
    public final double otherAllowance;
    public final double transportFee;
    public final double sportsFee;
}

