package com.example.PayrollService;

import com.example.PayrollService.config.RoleSalaryConfig;
import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.dto.integration.AttendanceDTO;
import com.example.PayrollService.dto.integration.UserDTO;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.feign.AttendanceServiceClient;
import com.example.PayrollService.feign.UserServiceClient;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.service.PayrollServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PayrollServiceUnitTest {
    private UserServiceClient userServiceClient;
    private AttendanceServiceClient attendanceServiceClient;
    private PayrollRepository payrollRepository;
    private ReimbursementRepository reimbursementRepository;
    private PayrollServiceImpl payrollServiceImpl;

    @BeforeEach
    void setup() {
        userServiceClient = mock(UserServiceClient.class);
        attendanceServiceClient = mock(AttendanceServiceClient.class);
        payrollRepository = mock(PayrollRepository.class);
        reimbursementRepository = mock(ReimbursementRepository.class);

        payrollServiceImpl = new PayrollServiceImpl(
                userServiceClient,
                attendanceServiceClient,
                payrollRepository,
                reimbursementRepository

        );
    }

    @Test
    void testCreatePayroll_withMockedUserAndAttendance() {
        String employeeId = "E101";
        int month = 5;
        int year = 2025;

        // Mock user service to return null (simulate service unavailable)
        when(userServiceClient.getUserDetails(employeeId))
                .thenReturn(null);

        when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year))
                .thenReturn(new AttendanceDTO(employeeId, month, year, 20, 2, 1));

        when(reimbursementRepository.findByEmployeeId(employeeId)).thenReturn(Collections.emptyList());
        when(payrollRepository.save(any())).thenAnswer(invocation -> {
            PayrollRecord record = invocation.getArgument(0);
            record.setId(1L);
            return record;
        });

        PayrollRequestDTO dto = new PayrollRequestDTO();
        dto.setEmployeeId(employeeId);
        dto.setMonth(month);
        dto.setYear(year);
        dto.setRole("ENGINEER"); // Set role in DTO for fallback scenario

        PayrollResponseDTO response = payrollServiceImpl.createPayroll(dto);

        assertNotNull(response);
        assertEquals(employeeId, response.getEmployeeId());
        assertTrue(response.getNetSalary() > 0);
    }

    @Test
    void createPayroll_WithApprovedReimbursements_IncludesReimbursementInNetSalary() {
        String employeeId = "E01";
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        // Mock user service to return null (simulate service unavailable)
        when(userServiceClient.getUserDetails(employeeId))
                .thenReturn(null);

        AttendanceDTO attendance = new AttendanceDTO(employeeId, month, year, 20, 2, 1);
        when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year))
                .thenReturn(attendance);

        PayrollRequestDTO request = new PayrollRequestDTO();
        request.setEmployeeId(employeeId);
        request.setMonth(month);
        request.setYear(year);
        request.setRole("ENGINEER"); // Set role in DTO for fallback scenario

        ReimbursementRecord reimbursement = new ReimbursementRecord();
        reimbursement.setEmployeeId(employeeId);
        reimbursement.setAmount(1500.0);
        reimbursement.setStatus("APPROVED");
        reimbursement.setRequestDate(LocalDate.of(year, month, 10));

        when(reimbursementRepository.findByEmployeeId(employeeId)).thenReturn(List.of(reimbursement));

        when(payrollRepository.save(any(PayrollRecord.class))).thenAnswer(inv -> {
            PayrollRecord r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        RoleSalaryConfig config = RoleSalaryConfig.ENGINEER;
        double basicSalary = config.getBasicSalary();
        double allowances = config.getMedicalAllowance() + config.getOtherAllowance();
        double tax = 0.10 * (basicSalary + allowances);
        double deductions = tax + config.getSportsFee() + config.getTransportFee();
        double noPay = (basicSalary / 20) * attendance.getNotApprovedLeaves();
        double expectedNet = (basicSalary + allowances) - deductions + reimbursement.getAmount() - noPay;

        PayrollResponseDTO response = payrollServiceImpl.createPayroll(request);

        assertNotNull(response);
        assertEquals(expectedNet, response.getNetSalary(), 0.01);
    }
}
