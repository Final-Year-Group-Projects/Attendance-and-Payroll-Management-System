package com.example.PayrollService;

import com.example.PayrollService.controller.*;
import com.example.PayrollService.dto.*;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.service.PayrollService;
import com.example.PayrollService.service.ReimbursementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollControllerTest {

    @Mock
    private PayrollService payrollService;
    @Mock
    private PayrollRepository payrollRepository;
    @Mock
    private ReimbursementService reimbursementService;
    @Mock
    private ReimbursementRepository reimbursementRepository;

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
    @InjectMocks
    private ReimbursementController reimbursementController;

    private PayrollRecord createTestPayrollRecord() {
        PayrollRecord record = new PayrollRecord();
        record.setId(1L);
        record.setEmployeeId("E01");
        record.setBasicSalary(50000.0);
        record.setMedicalAllowance(2000.0);
        record.setTransportFee(1000.0);
        record.setSportsFee(500.0);
        record.setTaxDeduction(5100.0);
        record.setNoPay(1666.67);
        record.setWorkingDays(30);
        record.setApprovedLeaves(2);
        record.setNotApprovedLeaves(1);
        record.setNetSalary(50000.0);
        record.setGeneratedDate(LocalDate.now());
        record.setMonth(LocalDate.now().getMonthValue());
        record.setYear(LocalDate.now().getYear());
        record.setStatus("GENERATED");
        return record;
    }

    @Test
    void createPayroll_ValidRequest_ReturnsCreatedResponse() {
        PayrollRequestDTO request = new PayrollRequestDTO();
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.createPayroll(any(PayrollRequestDTO.class))).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollCreationController.createPayroll(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getPayrollById_ExistingId_ReturnsPayroll() {
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.getPayrollById(1L)).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getPayrollById_NonExistingId_ReturnsNotFound() {
        when(payrollService.getPayrollById(1L)).thenReturn(null);

        ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updatePayroll_ValidRequest_ReturnsUpdatedPayroll() {
        PayrollRequestDTO request = new PayrollRequestDTO();
        PayrollResponseDTO mockResponse = new PayrollResponseDTO();
        when(payrollService.updatePayroll(1L, request)).thenReturn(mockResponse);

        ResponseEntity<PayrollResponseDTO> response = payrollUpdateController.updatePayroll(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void deletePayroll_ExistingId_ReturnsSuccessMessage() {
        when(payrollService.deletePayroll(1L)).thenReturn(true);

        ResponseEntity<String> response = payrollDeleteController.deletePayroll(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payroll deleted for user 1", response.getBody());
    }

    @Test
    void deletePayroll_NonExistingId_ReturnsNotFound() {
        when(payrollService.deletePayroll(1L)).thenReturn(false);

        ResponseEntity<String> response = payrollDeleteController.deletePayroll(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void notifyEmployee_Success_ReturnsOk() {
        PayrollNotificationResponseDTO successResponse = new PayrollNotificationResponseDTO();
        successResponse.setStatus("success");
        when(payrollService.generatePayrollNotification("E01")).thenReturn(successResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee("E01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notifyEmployee_Error_ReturnsNotFound() {
        PayrollNotificationResponseDTO errorResponse = new PayrollNotificationResponseDTO();
        errorResponse.setStatus("error");
        when(payrollService.generatePayrollNotification("E01")).thenReturn(errorResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee("E01");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPayslip_ExistingId_ReturnsHtmlContent() {
        PayrollRecord record = createTestPayrollRecord();
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(record));

        ResponseEntity<String> response = payslipController.getPayslip(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getPayslip_NonExistingId_ReturnsNotFoundWithHtmlMessage() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = payslipController.getPayslip(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void downloadPayslipPdf_ExistingId_ReturnsPdfContent() {
        PayrollRecord record = createTestPayrollRecord();
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(record));

        ResponseEntity<byte[]> response = payslipController.downloadPayslipPdf(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void downloadPayslipPdf_NonExistingId_ReturnsNotFound() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = payslipController.downloadPayslipPdf(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void submitReimbursementRequest_ValidRequest_ReturnsCreatedResponse() {
        ReimbursementRequestDTO request = new ReimbursementRequestDTO();
        request.setEmployeeId("E01");
        request.setType("travel");
        request.setAmount(1500.0);
        request.setDescription("Taxi fare");

        ReimbursementResponseDTO mockResponse = new ReimbursementResponseDTO();
        mockResponse.setEmployeeId("E01");

        when(reimbursementService.submitRequest(any())).thenReturn(mockResponse);

        ReimbursementResponseDTO response = reimbursementController.submitRequest(request);

        assertNotNull(response);
    }

    @Test
    void getRequestsByEmployee_ExistingEmployee_ReturnsReimbursementsList() {
        String employeeId = "E01";
        List<ReimbursementResponseDTO> mockResponses = Arrays.asList(
                new ReimbursementResponseDTO(),
                new ReimbursementResponseDTO()
        );
        when(reimbursementService.getRequestsByEmployee(employeeId)).thenReturn(mockResponses);

        List<ReimbursementResponseDTO> responses = reimbursementController.getRequestsByEmployee(employeeId);

        assertEquals(2, responses.size());
    }

    @Test
    void updateStatus_ValidRequest_ReturnsUpdatedReimbursement() {
        Long reimbursementId = 1L;
        String status = "APPROVED";

        ReimbursementResponseDTO mockResponse = new ReimbursementResponseDTO();
        mockResponse.setId(reimbursementId);
        mockResponse.setStatus(status);

        when(reimbursementService.updateStatus(reimbursementId, status)).thenReturn(mockResponse);

        ReimbursementResponseDTO response = reimbursementController.updateStatus(reimbursementId, status);

        assertEquals(status, response.getStatus());
    }

    @Test
    void deleteRequest_ExistingId_CallsServiceMethod() {
        Long reimbursementId = 1L;
        doNothing().when(reimbursementService).deleteRequest(reimbursementId);

        reimbursementController.deleteRequest(reimbursementId);

        verify(reimbursementService, times(1)).deleteRequest(reimbursementId);
    }

    @Test
    void deletePayrollsByEmployeeId_Found_ReturnsSuccessMessage() {
        String employeeId = "E123";
        when(payrollService.deletePayrollsByEmployeeId(employeeId)).thenReturn(true);

        ResponseEntity<?> response = payrollDeleteController.deletePayrollsByEmployeeId(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePayrollsByEmployeeId_NotFound_ReturnsNotFound() {
        String employeeId = "E999";
        when(payrollService.deletePayrollsByEmployeeId(employeeId)).thenReturn(false);

        ResponseEntity<?> response = payrollDeleteController.deletePayrollsByEmployeeId(employeeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
