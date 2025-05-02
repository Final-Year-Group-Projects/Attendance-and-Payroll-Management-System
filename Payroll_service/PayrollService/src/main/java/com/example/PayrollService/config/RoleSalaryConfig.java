package com.example.PayrollService.config;

public enum RoleSalaryConfig {
    ENGINEER(50000.0, 2000.0, 1000.0,1000.0, 500.0),
    MANAGER(70000.0, 3000.0, 1000.0,2000.0, 500.0),
    HR(40000.0, 1500.0, 1000.0,800.0, 500.0);

    public final double basicSalary;
    public final double medicalAllowance;
    public final double otherAllowance;
    public final double transportFee;
    public final double sportsFee;

    RoleSalaryConfig(double basicSalary, double medicalAllowance, double otherAllowance,double transportFee, double sportsFee) {
        this.basicSalary = basicSalary;
        this.medicalAllowance = medicalAllowance;
        this.otherAllowance = otherAllowance;
        this.transportFee = transportFee;
        this.sportsFee = sportsFee;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getMedicalAllowance() {
        return medicalAllowance;
    }

    public double getOtherAllowance() {
        return medicalAllowance;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public double getSportsFee() {
        return sportsFee;
    }
}

