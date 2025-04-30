package com.example.PayrollService;

import com.example.PayrollService.controller.*;
import com.example.PayrollService.dto.*;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.service.PayrollService;
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

    private PayrollRecord createTestPayrollRecord() {
        PayrollRecord record = new PayrollRecord();
        record.setEmployeeId(1L);
        record.setBasicSalary(5000.0);
        record.setWorkingDays(22);
        record.setApprovedLeaves(2);
        record.setNotApprovedLeaves(1);
        record.setDeductions(500.0);
        record.setNetSalary(4500.0);
        record.setGeneratedDate(LocalDate.now());
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
        assertTrue(response.getBody().startsWith("<html>"));
        assertTrue(response.getBody().contains("<h2>Payslip for Employee ID: 1</h2>"));
        assertTrue(response.getBody().contains("</html>"));
        assertTrue(response.getBody().contains("5000.0")); // Basic Salary
        assertTrue(response.getBody().contains("4500.0")); // Net Salary
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
}