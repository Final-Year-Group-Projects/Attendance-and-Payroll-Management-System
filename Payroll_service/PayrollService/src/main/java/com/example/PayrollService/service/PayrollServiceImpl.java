package com.example.PayrollService.service;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.repository.PayrollRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PayrollServiceImpl implements PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    @Override
    public PayrollResponseDTO createPayroll(PayrollRequestDTO dto){
        PayrollRecord payrollRecord = new PayrollRecord();

//        Double netSalary = ((dto.getBasicSalary()/30.0)*(dto.getWorkingDays()- dto.getLeavesTaken()))- dto.getDeductions();

        int payableDays = dto.getWorkingDays() - dto.getNotApprovedLeaves();
        double perDaySalary = dto.getBasicSalary() / dto.getWorkingDays();
        double gross = perDaySalary * payableDays;
        double netSalary = gross - dto.getDeductions();

        payrollRecord.setEmployeeId(dto.getEmployeeId());
        payrollRecord.setBasicSalary(dto.getBasicSalary());
        payrollRecord.setWorkingDays(dto.getWorkingDays());
        payrollRecord.setApprovedLeaves(dto.getApprovedLeaves());
        payrollRecord.setNotApprovedLeaves(dto.getNotApprovedLeaves());
        payrollRecord.setDeductions(dto.getDeductions());
        payrollRecord.setNetSalary(netSalary);
        payrollRecord.setGeneratedDate(LocalDate.now());

        PayrollRecord saved = payrollRepository.save(payrollRecord);

        PayrollResponseDTO response = new PayrollResponseDTO();
        response.setId(saved.getId());
        response.setEmployeeId(saved.getEmployeeId());
        response.setNetSalary(saved.getNetSalary());
        response.setGeneratedDate(saved.getGeneratedDate());

        return response;

    }

}