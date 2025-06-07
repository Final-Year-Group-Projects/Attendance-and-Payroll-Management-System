package com.example.PayrollService.service;

import com.example.PayrollService.config.RoleSalaryConfig;
import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.dto.PayrollNotificationResponseDTO;
import com.example.PayrollService.dto.integration.AttendanceDTO;
import com.example.PayrollService.dto.integration.UserDTO;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.exception.ValidationException;
import com.example.PayrollService.exception.ResourceNotFoundException;
import com.example.PayrollService.feign.AttendanceServiceClient;
import com.example.PayrollService.feign.UserServiceClient;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.util.ValidationUtils;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private AttendanceServiceClient attendanceServiceClient;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    // Add constants for validation rules
    private static final double MINIMUM_WAGE = 1000.00;
    private static final int MAX_WORKING_DAYS = 31;
    private static final List<String> VALID_PAYROLL_STATUSES = List.of("GENERATED", "PAID", "CANCELLED");

    // Validation helper methods
    private void validateWorkingDays(int workingDays, int approvedLeaves, int notApprovedLeaves) {
        int totalDays = workingDays + approvedLeaves + notApprovedLeaves;

        if (workingDays < 0 || approvedLeaves < 0 || notApprovedLeaves < 0) {
            throw new ValidationException(
                    "Days values cannot be negative. Received: " +
                            "workingDays=" + workingDays + ", " +
                            "approvedLeaves=" + approvedLeaves + ", " +
                            "notApprovedLeaves=" + notApprovedLeaves
            );
        }

        if (totalDays > MAX_WORKING_DAYS) {
            throw new ValidationException(
                    String.format(
                            "Total days (worked %d + leaves %d) exceed maximum %d days",
                            workingDays,
                            approvedLeaves + notApprovedLeaves,
                            MAX_WORKING_DAYS
                    )
            );
        }
    }

    private void validateNetSalary(double netSalary) {
        if (netSalary < MINIMUM_WAGE) {
            throw new ValidationException(
                    String.format(
                            "Calculated net salary %.2f is below minimum wage %.2f",
                            netSalary,
                            MINIMUM_WAGE
                    )
            );
        }

        if (netSalary <= 0) {
            throw new ValidationException(
                    "Net salary must be positive. Received: " + netSalary
            );
        }
    }

    private RoleSalaryConfig validateRoleConfiguration(String role) {
        try {
            return RoleSalaryConfig.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid role configuration: " + role);
        }
    }

    private void validateEmployeeExists(String employeeId) {
        if (!payrollRepository.existsByEmployeeId(employeeId)) {
            throw new ResourceNotFoundException("Employee not found: " + employeeId);
        }
    }

    private void validatePayrollRecordExists(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payroll record not found with ID: " + id);
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals("PAID") && (newStatus.equalsIgnoreCase("GENERATED") || newStatus.equalsIgnoreCase("CANCELLED"))) {
            throw new ValidationException("Cannot revert PAID request to "+ newStatus.toUpperCase());
        }
        if (currentStatus.equals("CANCELLED") && (newStatus.equalsIgnoreCase("GENERATED") || newStatus.equalsIgnoreCase("PAID"))) {
            throw new ValidationException("Cannot revert CANCELLED request to "+ newStatus.toUpperCase());
        }
    }

    @Override
    public PayrollResponseDTO createPayroll(PayrollRequestDTO dto) {

        // 1. Try to fetch user details with fallback and validations
        UserDTO user;
        String role;
        try {
            user = userServiceClient.getUserDetails(dto.getEmployeeId());
            if (user == null) {
                throw new ValidationException("User does not exist");
            }
            role = user.getRole();
            if (role == null || role.isBlank()) {
                // Handle scenario where role is null or blank
                throw new ValidationException("User role is null or blank from UserService response");
            }
        } catch (Exception e) {
            System.err.println("UserService unavailable");
            if (dto.getRole() == null) {
                throw new ValidationException(
                        "Role is required when UserService is unavailable");
            }
            role = dto.getRole(); // Use Postman-provided role
        }
        RoleSalaryConfig config = validateRoleConfiguration(dto.getRole());


        // 2. Try to fetch attendance details with fallback and validations
        AttendanceDTO attendance;
        try {
            attendance = attendanceServiceClient.getAttendanceDetails(
                    dto.getEmployeeId(), dto.getMonth(), dto.getYear());

            // Check if the service returned null or if any critical fields are null
            if (attendance == null || attendance.getWorkingDays() == null ||
                    attendance.getApprovedLeaves() == null || attendance.getNotApprovedLeaves() == null) {
                throw new ValidationException("Attendance service returned incomplete data. All fields (working days, approved leaves, not approved leaves) must be present.");
            }

//            validateWorkingDays(attendance.getWorkingDays(), attendance.getApprovedLeaves(), attendance.getNotApprovedLeaves());
        } catch (Exception e) {
            System.err.println("AttendanceService unavailable");
            if (dto.getWorkingDays() == null ||
                    dto.getApprovedLeaves() == null ||
                    dto.getNotApprovedLeaves() == null) {
                throw new ValidationException(
                        "Attendance data is required when AttendanceService is unavailable");
            }
            attendance = new AttendanceDTO(
                    dto.getEmployeeId(),
                    dto.getMonth(),
                    dto.getYear(),
                    dto.getWorkingDays(),
                    dto.getApprovedLeaves(),
                    dto.getNotApprovedLeaves()
            );
        }
        int workingDays = attendance.getWorkingDays();
        int approvedLeaves = attendance.getApprovedLeaves();
        int notApprovedLeaves = attendance.getNotApprovedLeaves();
        validateWorkingDays(workingDays, approvedLeaves, notApprovedLeaves);


        double basicSalary = config.getBasicSalary();
        double medicalAllowance = config.getMedicalAllowance();
        double otherAllowance = config.getOtherAllowance();
        double transportFee = config.getTransportFee();
        double sportsFee = config.getSportsFee();

        double totalAllowance = medicalAllowance + otherAllowance;

        // Calculate payable days and no pay deduction
        int totalWorkingDays=20;  //total working days for month

        int payableDays = workingDays + approvedLeaves;
        double noPay = (basicSalary / totalWorkingDays) * notApprovedLeaves;

        // Calculate gross salary based on payable days and allowances
        double gross_salary = basicSalary + totalAllowance;

        //  Calculate tax (e.g., 10% of basic + allowance)
        double tax = 0.10 * gross_salary;

        // Calculate total deductions
        double totalDeductions = tax + sportsFee + transportFee;

        // Fetch approved reimbursements for current month/year
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        // Convert employeeId to String for repository call
        String employeeId = dto.getEmployeeId().toString();

        List<ReimbursementRecord> approvedReimbursements = reimbursementRepository.findByEmployeeId(employeeId)
                .stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus())
                        && r.getRequestDate().getMonthValue() == month
                        && r.getRequestDate().getYear() == year)
                .toList();

        double totalReimbursements = approvedReimbursements.stream()
                .mapToDouble(ReimbursementRecord::getAmount)
                .sum();

        // Calculate net salary
        double netSalary = gross_salary - totalDeductions + totalReimbursements - noPay;
        validateNetSalary(netSalary);

        // Create and save PayrollRecord entity
        PayrollRecord payrollRecord = new PayrollRecord();
        payrollRecord.setEmployeeId(employeeId);
        payrollRecord.setBasicSalary(basicSalary);
        payrollRecord.setMedicalAllowance(medicalAllowance);
        payrollRecord.setTransportFee(transportFee);
        payrollRecord.setSportsFee(sportsFee);
        payrollRecord.setTaxDeduction(tax);
        payrollRecord.setNoPay(noPay);
        payrollRecord.setWorkingDays(workingDays);
        payrollRecord.setApprovedLeaves(approvedLeaves);
        payrollRecord.setNotApprovedLeaves(notApprovedLeaves);
        payrollRecord.setNetSalary(netSalary);
        payrollRecord.setGeneratedDate(today);
        payrollRecord.setMonth(month);
        payrollRecord.setYear(year);
        payrollRecord.setStatus("GENERATED");

        PayrollRecord saved = payrollRepository.save(payrollRecord);

        // Optional: simulate notification
        System.out.printf("Notification simulated: Sent payroll (ID: %d, Net Salary: %.2f, Date: %s) for employee ID: %s%n",
                saved.getId(), saved.getNetSalary(), saved.getGeneratedDate(), saved.getEmployeeId());

        // Prepare and return response DTO
        return PayrollResponseDTO.fromRecord(saved);
    }

    @Override
    public PayrollResponseDTO getPayrollById(Long id) {
        validatePayrollRecordExists(id);
        Optional<PayrollRecord> payrollRecord = payrollRepository.findById(id);
        if (payrollRecord.isPresent()) {
            return PayrollResponseDTO.fromRecord(payrollRecord.get());
        }
        return null;
    }

    @Override
    public PayrollResponseDTO updatePayroll(Long id, PayrollRequestDTO dto) {
        validatePayrollRecordExists(id);

        Optional<PayrollRecord> payrollRecordOptional = payrollRepository.findById(id);
        if (!payrollRecordOptional.isPresent()) {
            return null;
        }

        PayrollRecord payrollRecord = payrollRecordOptional.get();

        // 1. Try to fetch user details (role) with fallback
        UserDTO user;
        String role;
        try {
            user = userServiceClient.getUserDetails(dto.getEmployeeId());
            if (user == null) {
                throw new ValidationException("Employee ID does not exist");
            }
            role = user.getRole();
        } catch (Exception e) {
            System.err.println("UserService unavailable");
            if (dto.getRole() == null) {
                throw new ValidationException(
                        "Role is required when UserService is unavailable");
            }
            role = dto.getRole();

        }

        // 2. Try to fetch attendance details with fallback
        AttendanceDTO attendance;
        try {
            attendance = attendanceServiceClient.getAttendanceDetails(
                    dto.getEmployeeId(), dto.getMonth(), dto.getYear());
//            validateWorkingDays(attendance.getWorkingDays(), attendance.getApprovedLeaves(), attendance.getNotApprovedLeaves());
        } catch (Exception e) {
            System.err.println("AttendanceService unavailable");
            if (dto.getWorkingDays() == null ||
                    dto.getApprovedLeaves() == null ||
                    dto.getNotApprovedLeaves() == null) {
                throw new ValidationException(
                        "Attendance data is required when AttendanceService is unavailable");
            }
            attendance = new AttendanceDTO(
                    dto.getEmployeeId(),
                    dto.getMonth(),
                    dto.getYear(),
                    dto.getWorkingDays(),
                    dto.getApprovedLeaves(),
                    dto.getNotApprovedLeaves()
            );
        }

        int workingDays = attendance.getWorkingDays();
        int approvedLeaves = attendance.getApprovedLeaves();
        int notApprovedLeaves = attendance.getNotApprovedLeaves();

        // Service-layer validations
        validateWorkingDays(workingDays, approvedLeaves, notApprovedLeaves);

        // 1. Get role and fetch fixed salary config
        RoleSalaryConfig config;
        try {
            config = RoleSalaryConfig.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid role: " + role);
        }

        double basicSalary = config.getBasicSalary();
        double medicalAllowance = config.getMedicalAllowance();
        double otherAllowance = config.getOtherAllowance();
        double transportFee = config.getTransportFee();
        double sportsFee = config.getSportsFee();

        double totalAllowance = medicalAllowance + otherAllowance;

        // 2. Calculate payable days and no pay deduction
        int totalWorkingDays=20;  //total working days for month

        int payableDays = workingDays + approvedLeaves;
        double noPay = (basicSalary / totalWorkingDays) * notApprovedLeaves;

        // 3. Calculate gross salary based on payable days and allowances
        double gross_salary = basicSalary + totalAllowance;

        // 4. Calculate tax (e.g., 10% of basic + allowance)
        double tax = 0.10 * gross_salary;

        // 5. Calculate total deductions
        double totalDeductions = tax + sportsFee + transportFee;

        // 6. Fetch approved reimbursements for current month/year
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        // Convert employeeId to String for repository call
        String employeeId = dto.getEmployeeId().toString();

        List<ReimbursementRecord> approvedReimbursements = reimbursementRepository.findByEmployeeId(employeeId)
                .stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus())
                        && r.getRequestDate().getMonthValue() == month
                        && r.getRequestDate().getYear() == year)
                .toList();

        double totalReimbursements = approvedReimbursements.stream()
                .mapToDouble(ReimbursementRecord::getAmount)
                .sum();

        // 7. Calculate net salary
        double netSalary = gross_salary - totalDeductions + totalReimbursements - noPay;
        validateNetSalary(netSalary);

        // 8. Update the payroll record
        payrollRecord.setEmployeeId(employeeId);
        payrollRecord.setBasicSalary(basicSalary);
        payrollRecord.setMedicalAllowance(medicalAllowance);
        payrollRecord.setTransportFee(transportFee);
        payrollRecord.setSportsFee(sportsFee);
        payrollRecord.setTaxDeduction(tax);
        payrollRecord.setNoPay(noPay);
        payrollRecord.setWorkingDays(workingDays);
        payrollRecord.setApprovedLeaves(approvedLeaves);
        payrollRecord.setNotApprovedLeaves(notApprovedLeaves);
        payrollRecord.setNetSalary(netSalary);
        payrollRecord.setGeneratedDate(today);
        payrollRecord.setMonth(month);
        payrollRecord.setYear(year);
        payrollRecord.setStatus("UPDATED");

        payrollRecord.setUpdatedDate(LocalDateTime.now());

        PayrollRecord updated = payrollRepository.save(payrollRecord);

        // 9. Prepare and return response DTO
        return PayrollResponseDTO.fromRecord(updated);
    }

    @Override
    public boolean deletePayroll(Long id) {
        validatePayrollRecordExists(id);
//        if (!payrollRepository.existsById(id)) {
//            return false;
//        }
        payrollRepository.deleteById(id);
        return true;
    }

    @Override
    public List<PayrollResponseDTO> getPayrollsByEmployeeId(String employeeId) {
        validateEmployeeExists(employeeId);
        List<PayrollRecord> records = payrollRepository.findByEmployeeId(employeeId);
        return records.stream()
                .map(PayrollResponseDTO::fromRecord)
                .toList();
    }

    @Override
    public void generatePayrollsForAllEmployees(Integer month, Integer year) {
        List<String> allEmployeeIds = payrollRepository.findAllEmployeeIdsDistinct();

        for (String empId : allEmployeeIds) {
            try {
                PayrollRequestDTO dto = new PayrollRequestDTO();
                dto.setEmployeeId(empId);
                dto.setMonth(month);
                dto.setYear(year);

                // This will now automatically fetch role and attendance via createPayroll()
                createPayroll(dto);
            } catch (Exception e) {
                System.err.println("Failed to generate payroll for " + empId + ": " + e.getMessage());
            }
        }
        System.out.println("Generated payrolls for all employees.");
    }

    @Override
    public List<PayrollResponseDTO> getAllPayrolls() {
        List<PayrollRecord> records = payrollRepository.findAll();
        return records.stream()
                .map(PayrollResponseDTO::fromRecord)
                .toList();
    }

    @Override
    public PayrollNotificationResponseDTO generatePayrollNotification(String employeeId) {
        List<PayrollRecord> payrolls = payrollRepository.findByEmployeeId(employeeId);

        if (payrolls.isEmpty()) {
            throw new ResourceNotFoundException("No payroll records found for employee ID: " + employeeId);
        }

        PayrollRecord latest = payrolls.get(payrolls.size() - 1);
        PayrollResponseDTO payrollDTO = PayrollResponseDTO.fromRecord(latest);

        String message = String.format(
                "Payroll notification sent: ID %d, Net Salary %.2f, Date %s",
                payrollDTO.getId(),
                payrollDTO.getNetSalary(),
                payrollDTO.getGeneratedDate()
        );

        return new PayrollNotificationResponseDTO("success", message, employeeId);
    }

    @Override
    public PayrollResponseDTO updatePayrollStatus(Long id, String status) {
        validatePayrollRecordExists(id);
        ValidationUtils.validateInList(status, VALID_PAYROLL_STATUSES, "payroll status");

//        if (!VALID_PAYROLL_STATUSES.contains(status.toUpperCase())) {
//            throw new ValidationException("Invalid payroll status: " + status);
//        }
        Optional<PayrollRecord> payrollRecordOptional = payrollRepository.findById(id);
//        if (!payrollRecordOptional.isPresent()) {
//            throw new ResourceNotFoundException("Payroll record not found with ID: " + id);
//        }

        PayrollRecord payrollRecord = payrollRecordOptional.get();
        validateStatusTransition(payrollRecord.getStatus(), status);
        payrollRecord.setStatus(status);
        payrollRecord.setUpdatedDate(LocalDateTime.now());
        PayrollRecord updated = payrollRepository.save(payrollRecord);

        return PayrollResponseDTO.fromRecord(updated);
    }

    @Override
    @Transactional
    public boolean deletePayrollsByEmployeeId(String employeeId) {
        validateEmployeeExists(employeeId);
        List<PayrollRecord> records = payrollRepository.findByEmployeeId(employeeId);
        if (records.isEmpty()) {
            return false;
        }
        payrollRepository.deleteByEmployeeId(employeeId);
        return true;
    }

}