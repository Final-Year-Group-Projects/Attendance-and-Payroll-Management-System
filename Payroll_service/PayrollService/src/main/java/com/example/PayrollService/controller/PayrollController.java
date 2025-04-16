package com.example.PayrollService.controller;

import com.example.PayrollService.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PayrollController {
    @Autowired
    private PayrollService payrollService;


    @GetMapping("/")
    public String home() {
        return "Payroll Service is running!";
    }


}
