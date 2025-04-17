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
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PayrollServiceImpl implements PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    @Override
    public PayrollResponseDTO createPayroll(PayrollRequestDTO dto) {
        int payableDays = dto.getWorkingDays() - dto.getNotApprovedLeaves();
        double perDaySalary = dto.getBasicSalary() / dto.getWorkingDays();
        double gross = perDaySalary * payableDays;
        double netSalary = gross - dto.getDeductions();

        PayrollRecord payrollRecord = new PayrollRecord();
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

    @Override
    public PayrollResponseDTO getPayrollById(Long id) {
        Optional<PayrollRecord> payrollRecord = payrollRepository.findById(id);
        if (payrollRecord.isPresent()) {
            PayrollRecord record = payrollRecord.get();
            PayrollResponseDTO response = new PayrollResponseDTO();
            response.setId(record.getId());
            response.setEmployeeId(record.getEmployeeId());
            response.setNetSalary(record.getNetSalary());
            response.setGeneratedDate(record.getGeneratedDate());
            return response;
        }
        return null;
    }

    @Override
    public PayrollResponseDTO updatePayroll(Long id, PayrollRequestDTO dto) {
        Optional<PayrollRecord> payrollRecordOptional = payrollRepository.findById(id);
        if (!payrollRecordOptional.isPresent()) {
            return null;
        }

        PayrollRecord payrollRecord = payrollRecordOptional.get();

        int payableDays = dto.getWorkingDays() - dto.getNotApprovedLeaves();
        double perDaySalary = dto.getBasicSalary() / dto.getWorkingDays();
        double gross = perDaySalary * payableDays;
        double netSalary = gross - dto.getDeductions();

        payrollRecord.setBasicSalary(dto.getBasicSalary());
        payrollRecord.setWorkingDays(dto.getWorkingDays());
        payrollRecord.setApprovedLeaves(dto.getApprovedLeaves());
        payrollRecord.setNotApprovedLeaves(dto.getNotApprovedLeaves());
        payrollRecord.setDeductions(dto.getDeductions());
        payrollRecord.setNetSalary(netSalary);
        payrollRecord.setGeneratedDate(LocalDate.now());

        PayrollRecord updated = payrollRepository.save(payrollRecord);

        PayrollResponseDTO response = new PayrollResponseDTO();
        response.setId(updated.getId());
        response.setEmployeeId(updated.getEmployeeId());
        response.setNetSalary(updated.getNetSalary());
        response.setGeneratedDate(updated.getGeneratedDate());

        return response;
    }

    @Override
    public boolean deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            return false;
        }
        payrollRepository.deleteById(id);
        return true;
    }
}