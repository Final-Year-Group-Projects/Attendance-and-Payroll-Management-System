package com.example.PayrollService;

import com.example.PayrollService.config.RoleSalaryConfig;
import com.example.PayrollService.controller.*;
import com.example.PayrollService.dto.*;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.service.PayrollService;
import com.example.PayrollService.controller.ReimbursementController;
import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.service.PayrollServiceImpl;
import com.example.PayrollService.service.ReimbursementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollControllerTest {

    @Mock
    private PayrollService payrollService;

    @Mock
    private PayrollRepository payrollRepository;

    @InjectMocks
    private PayrollCreationController payrollCreationController;

    @InjectMocks
    private PayrollReadController payrollReadController;

    @InjectMocks
    private PayrollUpdateController payrollUpdateController;

    @InjectMocks
    private PayrollDeleteController payrollDeleteController;

    @InjectMocks
    private PayrollNotifyController payrollNotifyController;

    @InjectMocks
    private PayslipController payslipController;

    @Mock
    private ReimbursementService reimbursementService;

    @Mock
    private ReimbursementRepository reimbursementRepository;

    @InjectMocks
    private ReimbursementController reimbursementController;

    @InjectMocks
    private PayrollServiceImpl payrollServiceImpl;


    private PayrollRecord createTestPayrollRecord() {
        PayrollRecord record = new PayrollRecord();
        record.setId(1L);
        record.setEmployeeId(1L);

        // Assume role is ENGINEER for this test
        // You can fetch these from RoleSalaryConfig if you want dynamic values
        record.setBasicSalary(50000.0);
        record.setMedicalAllowance(2000.0);
        record.setTransportFee(1000.0);
        record.setSportsFee(500.0);
        record.setTaxDeduction(5100.0); // Example: 10% of (basic + allowances)
        record.setNoPay(1666.67);       // Example: (basic/workingDays) * notApprovedLeaves

        record.setWorkingDays(30);
        record.setApprovedLeaves(2);
        record.setNotApprovedLeaves(1);

        // Calculate net salary for test (gross - deductions - no pay)
        double gross = (50000.0 / 30) * (30 - 1) + 2000.0 + 1000.0;
        double totalDeductions = 5100.0 + 500.0;
        double netSalary = gross - totalDeductions - 1666.67;

        record.setNetSalary(netSalary);
        record.setGeneratedDate(LocalDate.now());
        record.setMonth(LocalDate.now().getMonthValue());
        record.setYear(LocalDate.now().getYear());
        record.setStatus("GENERATED");

        return record;
    }


    private ReimbursementRecord createTestReimbursementRecord() {
        ReimbursementRecord record = new ReimbursementRecord();
        record.setId(1L);
        record.setEmployeeId(1L);
        record.setType("travel");
        record.setAmount(1500.0);
        record.setDescription("Taxi fare for client meeting");
        record.setStatus("APPROVED");
        record.setRequestDate(LocalDate.now());
        return record;
    }


    // ==================== PayrollCreationController Tests ====================
    @Test
    void createPayroll_ValidRequest_ReturnsCreatedResponse() {
        PayrollRequestDTO request = new PayrollRequestDTO();
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.createPayroll(any(PayrollRequestDTO.class))).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollCreationController.createPayroll(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(payrollService, times(1)).createPayroll(any(PayrollRequestDTO.class));
    }

    // ==================== PayrollReadController Tests ====================
    @Test
    void getPayrollById_ExistingId_ReturnsPayroll() {
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.getPayrollById(1L)).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(payrollService, times(1)).getPayrollById(1L);
    }

    @Test
    void getPayrollById_NonExistingId_ReturnsNotFound() {
        when(payrollService.getPayrollById(1L)).thenReturn(null);

        ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ==================== PayrollUpdateController Tests ====================
    @Test
    void updatePayroll_ValidRequest_ReturnsUpdatedPayroll() {
        PayrollRequestDTO request = new PayrollRequestDTO();
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.updatePayroll(1L, request)).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollUpdateController.updatePayroll(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(payrollService, times(1)).updatePayroll(1L, request);
    }

    // ==================== PayrollDeleteController Tests ====================
    @Test
    void deletePayroll_ExistingId_ReturnsSuccessMessage() {
        when(payrollService.deletePayroll(1L)).thenReturn(true);

        ResponseEntity<String> response = payrollDeleteController.deletePayroll(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payroll deleted for user 1", response.getBody());
        verify(payrollService, times(1)).deletePayroll(1L);
    }

    @Test
    void deletePayroll_NonExistingId_ReturnsNotFound() {
        when(payrollService.deletePayroll(1L)).thenReturn(false);

        ResponseEntity<String> response = payrollDeleteController.deletePayroll(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No payroll record found for user 1", response.getBody());
    }

    // ==================== PayrollNotifyController Tests ====================
    @Test
    void notifyEmployee_Success_ReturnsOk() {
        PayrollNotificationResponseDTO successResponse = new PayrollNotificationResponseDTO();
        successResponse.setStatus("success");
        when(payrollService.generatePayrollNotification(1L)).thenReturn(successResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(payrollService, times(1)).generatePayrollNotification(1L);
    }

    @Test
    void notifyEmployee_Error_ReturnsNotFound() {
        PayrollNotificationResponseDTO errorResponse = new PayrollNotificationResponseDTO();
        errorResponse.setStatus("error");
        when(payrollService.generatePayrollNotification(1L)).thenReturn(errorResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }

    // ==================== PayslipController Tests ====================
    @Test
    void getPayslip_ExistingId_ReturnsHtmlContent() {
        PayrollRecord record = createTestPayrollRecord();
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(record));

        ResponseEntity<String> response = payslipController.getPayslip(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("<html>"));
        assertTrue(response.getBody().contains("<h2>Payslip for Employee ID: 1</h2>"));
        assertTrue(response.getBody().contains("</html>"));
        assertTrue(response.getBody().matches("(?s).*Basic Salary.*50000.*"));
        assertTrue(response.getBody().contains(String.format("%.2f", record.getNetSalary())));  // Net Salary
    }

    @Test
    void getPayslip_NonExistingId_ReturnsNotFoundWithHtmlMessage() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = payslipController.getPayslip(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Payslip not found for ID 1"));
    }

    @Test
    void downloadPayslipPdf_ExistingId_ReturnsPdfContent() {
        PayrollRecord record = createTestPayrollRecord();
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(record));

        ResponseEntity<byte[]> response = payslipController.downloadPayslipPdf(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0); // Verify we got PDF content
        // Basic PDF header verification (optional)
        assertTrue(response.getBody()[0] == '%' && response.getBody()[1] == 'P' &&
                response.getBody()[2] == 'D' && response.getBody()[3] == 'F');
    }

    @Test
    void downloadPayslipPdf_NonExistingId_ReturnsNotFound() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = payslipController.downloadPayslipPdf(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

// ==================== ReimbursementController Tests ====================

    @Test
    void submitReimbursementRequest_ValidRequest_ReturnsCreatedResponse() {
        // Arrange
        ReimbursementRequestDTO request = new ReimbursementRequestDTO();
        request.setEmployeeId(1L);
        request.setType("travel");
        request.setAmount(1500.0);
        request.setDescription("Taxi fare for client meeting");

        ReimbursementResponseDTO mockResponse = new ReimbursementResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setEmployeeId(1L);
        mockResponse.setType("travel");
        mockResponse.setAmount(1500.0);
        mockResponse.setDescription("Taxi fare for client meeting");
        mockResponse.setStatus("PENDING");
        mockResponse.setRequestDate(LocalDate.now());

        when(reimbursementService.submitRequest(any(ReimbursementRequestDTO.class))).thenReturn(mockResponse);

        // Act
        ReimbursementResponseDTO response = reimbursementController.submitRequest(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(reimbursementService, times(1)).submitRequest(any(ReimbursementRequestDTO.class));
    }

    @Test
    void getRequestsByEmployee_ExistingEmployee_ReturnsReimbursementsList() {
        // Arrange
        Long employeeId = 1L;
        List<ReimbursementResponseDTO> mockResponses = Arrays.asList(
                new ReimbursementResponseDTO(1L, employeeId, "travel", 1500.0, "Taxi fare", "APPROVED", LocalDate.now()),
                new ReimbursementResponseDTO(2L, employeeId, "medical", 2000.0, "Doctor visit", "PENDING", LocalDate.now())
        );

        when(reimbursementService.getRequestsByEmployee(employeeId)).thenReturn(mockResponses);

        // Act
        List<ReimbursementResponseDTO> responses = reimbursementController.getRequestsByEmployee(employeeId);

        // Assert
        assertEquals(2, responses.size());
        assertEquals(mockResponses, responses);
        verify(reimbursementService, times(1)).getRequestsByEmployee(employeeId);
    }

    @Test
    void updateStatus_ValidRequest_ReturnsUpdatedReimbursement() {
        // Arrange
        Long reimbursementId = 1L;
        String status = "APPROVED";

        ReimbursementResponseDTO mockResponse = new ReimbursementResponseDTO();
        mockResponse.setId(reimbursementId);
        mockResponse.setStatus(status);

        when(reimbursementService.updateStatus(reimbursementId, status)).thenReturn(mockResponse);

        // Act
        ReimbursementResponseDTO response = reimbursementController.updateStatus(reimbursementId, status);

        // Assert
        assertEquals(status, response.getStatus());
        verify(reimbursementService, times(1)).updateStatus(reimbursementId, status);
    }

    @Test
    void deleteRequest_ExistingId_CallsServiceMethod() {
        // Arrange
        Long reimbursementId = 1L;
        doNothing().when(reimbursementService).deleteRequest(reimbursementId);

        // Act
        reimbursementController.deleteRequest(reimbursementId);

        // Assert
        verify(reimbursementService, times(1)).deleteRequest(reimbursementId);
    }

    @Test
    void createPayroll_WithApprovedReimbursements_IncludesReimbursementInNetSalary() {
        // Arrange
        PayrollRequestDTO request = new PayrollRequestDTO();
        request.setEmployeeId(1L);
        request.setWorkingDays(30);
        request.setApprovedLeaves(2);
        request.setNotApprovedLeaves(1);
        request.setRole("ENGINEER");  // Use role instead of basicSalary/deductions

        // Mock approved reimbursements
        List<ReimbursementRecord> approvedReimbursements = Arrays.asList(createTestReimbursementRecord());
        when(reimbursementRepository.findByEmployeeId(1L)).thenReturn(approvedReimbursements);

        // Mock payrollRepository.save to simulate DB save and ID generation
        when(payrollRepository.save(any(PayrollRecord.class))).thenAnswer(invocation -> {
            PayrollRecord record = invocation.getArgument(0);
            record.setId(1L); // simulate DB ID generation
            return record;
        });

        // Calculate expected net salary based on RoleSalaryConfig for ENGINEER
        RoleSalaryConfig config = RoleSalaryConfig.valueOf("ENGINEER");
        double basicSalary = config.getBasicSalary();
        double medicalAllowance = config.getMedicalAllowance();
        double transportFee = config.getTransportFee();
        double sportsFee = config.getSportsFee();

        double totalAllowance = medicalAllowance + transportFee;

        int payableDays = request.getWorkingDays() - request.getNotApprovedLeaves();
        double noPay = (basicSalary / request.getWorkingDays()) * request.getNotApprovedLeaves();

        double gross = (basicSalary / request.getWorkingDays()) * payableDays + totalAllowance;

        double tax = 0.10 * (basicSalary + totalAllowance);
        double totalDeductions = tax + sportsFee;

        double totalReimbursements = approvedReimbursements.stream()
                .mapToDouble(ReimbursementRecord::getAmount)
                .sum();

        double expectedNetSalary = gross - totalDeductions + totalReimbursements - noPay;

        // Act
        PayrollResponseDTO response = payrollServiceImpl.createPayroll(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(expectedNetSalary, response.getNetSalary(), 0.01);
    }

}