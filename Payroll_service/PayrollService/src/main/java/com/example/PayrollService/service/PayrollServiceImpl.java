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
import java.util.List;
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

        String simulatedMessage = String.format(
                "Notification simulated: Sent payroll (ID: %d, Net Salary: %.2f, Date: %s) for employee ID: %d",
                saved.getId(),
                saved.getNetSalary(),
                saved.getGeneratedDate(),
                saved.getEmployeeId()
        );
        System.out.println(simulatedMessage);

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

    @Override
    public List<PayrollResponseDTO> getPayrollsByEmployeeId(Long employeeId) {
        List<PayrollRecord> records = payrollRepository.findByEmployeeId(employeeId);
        return records.stream().map(record -> {
            PayrollResponseDTO dto = new PayrollResponseDTO();
            dto.setId(record.getId());
            dto.setEmployeeId(record.getEmployeeId());
            dto.setNetSalary(record.getNetSalary());
            dto.setGeneratedDate(record.getGeneratedDate());
            return dto;
        }).toList();
    }

    @Override
    public void generatePayrollsForAllEmployees(Integer month, Integer year) {
        // TODO: Replace this with your actual method to get employee IDs
        List<Long> allEmployeeIds = payrollRepository.findAllEmployeeIdsDistinct();

        for (Long empId : allEmployeeIds) {
            // Simulate or fetch required data per employee
            PayrollRequestDTO dto = new PayrollRequestDTO();
            dto.setEmployeeId(empId);
            dto.setBasicSalary(30000.0);  // Dummy value
            dto.setWorkingDays(30);     // Dummy value
            dto.setApprovedLeaves(2);   // Dummy value
            dto.setNotApprovedLeaves(1); // Dummy value
            dto.setDeductions(500.0);     // Dummy value

            createPayroll(dto);
        }

        System.out.println("Generated payrolls for all employees.");
    }

    @Override
    public List<PayrollResponseDTO> getAllPayrolls() {
        List<PayrollRecord> records = payrollRepository.findAll();
        return records.stream().map(record -> {
            PayrollResponseDTO dto = new PayrollResponseDTO();
            dto.setId(record.getId());
            dto.setEmployeeId(record.getEmployeeId());
            dto.setNetSalary(record.getNetSalary());
            dto.setGeneratedDate(record.getGeneratedDate());
            return dto;
        }).toList();
    }



}