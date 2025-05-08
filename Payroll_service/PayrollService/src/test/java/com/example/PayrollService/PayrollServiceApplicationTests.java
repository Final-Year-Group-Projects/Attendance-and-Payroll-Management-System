package com.example.PayrollService;

import com.example.PayrollService.config.FeignTestConfig;
import com.example.PayrollService.config.RoleSalaryConfig;
import com.example.PayrollService.controller.*;
import com.example.PayrollService.dto.*;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.service.PayrollService;
import com.example.PayrollService.controller.ReimbursementController;
import com.example.PayrollService.dto.ReimbursementRequestDTO;
import com.example.PayrollService.dto.ReimbursementResponseDTO;
import com.example.PayrollService.dto.integration.AttendanceDTO;
import com.example.PayrollService.dto.integration.UserDTO;
import com.example.PayrollService.entity.ReimbursementRecord;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.service.PayrollServiceImpl;
import com.example.PayrollService.service.ReimbursementService;
import feign.FeignException;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import feign.Request;
import feign.RetryableException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Import(FeignTestConfig.class)
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

    @Mock
    private com.example.PayrollService.feign.UserServiceClient userServiceClient;

    @Mock
    private com.example.PayrollService.feign.AttendanceServiceClient attendanceServiceClient;


    private PayrollRecord createTestPayrollRecord() {
        PayrollRecord record = new PayrollRecord();
        record.setId(1L);
        record.setEmployeeId("E01");

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
        record.setEmployeeId("E01");
        record.setType("travel");
        record.setAmount(1500.0);
        record.setDescription("Taxi fare for client meeting");
        record.setStatus("APPROVED");
        record.setRequestDate(LocalDate.now());
        return record;
    }

    @Test
    void testFeignClients() {
        // Arrange: mock the userServiceClient to return a valid UserDTO
        UserDTO mockUser = new UserDTO();
        mockUser.setEmployeeId("E100");
        mockUser.setRole("ENGINEER");

        AttendanceDTO mockAttendance = new AttendanceDTO();
        mockAttendance.setEmployeeId("E100");
        mockAttendance.setMonth(5);
        mockAttendance.setYear(2025);
        mockAttendance.setWorkingDays(20);
        mockAttendance.setApprovedLeaves(2);
        mockAttendance.setNotApprovedLeaves(1);

        when(userServiceClient.getUserDetails("E100")).thenReturn(mockUser);
        when(attendanceServiceClient.getAttendanceDetails("E100", 5, 2025)).thenReturn(mockAttendance);

        // Act
        UserDTO user = userServiceClient.getUserDetails("E100");
        AttendanceDTO attendance = attendanceServiceClient.getAttendanceDetails("E100", 5, 2025);

        // Assert
        assertNotNull(user);
        assertNotNull(user.getRole());
        assertTrue(attendance.getWorkingDays() > 0);
    }


    @Test
    void testCreatePayroll_withMockedUserAndAttendance() {
        // Arrange
        String employeeId = "E101";
        int month = 5;
        int year = 2025;

        // Mock user and attendance Feign client responses
        Mockito.when(userServiceClient.getUserDetails(employeeId))
                .thenReturn(new UserDTO(employeeId, "ENGINEER"));
        Mockito.when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year))
                .thenReturn(new AttendanceDTO(employeeId, month, year, 20, 2, 1));

        // Prepare complete request DTO
        PayrollRequestDTO dto = new PayrollRequestDTO();
        dto.setEmployeeId(employeeId);
        dto.setMonth(month);
        dto.setYear(year);
        // Mock other dependencies
        Mockito.when(reimbursementRepository.findByEmployeeId(employeeId))
                .thenReturn(Collections.emptyList());
        Mockito.when(payrollRepository.save(Mockito.any()))
                .thenAnswer(invocation -> {
                    PayrollRecord record = invocation.getArgument(0);
                    record.setId(1L);
                    return record;
                });

        // Act
        PayrollResponseDTO response = payrollServiceImpl.createPayroll(dto);

        // Assert
        assertNotNull(response);
        assertEquals(employeeId, response.getEmployeeId());
        assertTrue(response.getNetSalary() > 0);

        // Verify service interactions
        Mockito.verify(userServiceClient).getUserDetails(employeeId);
        Mockito.verify(attendanceServiceClient).getAttendanceDetails(employeeId, month, year);
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
        when(payrollService.generatePayrollNotification("E01")).thenReturn(successResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee("E01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(payrollService, times(1)).generatePayrollNotification("E01");
    }

    @Test
    void notifyEmployee_Error_ReturnsNotFound() {
        PayrollNotificationResponseDTO errorResponse = new PayrollNotificationResponseDTO();
        errorResponse.setStatus("error");
        when(payrollService.generatePayrollNotification("E01")).thenReturn(errorResponse);

        ResponseEntity<PayrollNotificationResponseDTO> response = payrollNotifyController.notifyEmployee("E01");

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
        // Check HTML tags
        assertTrue(response.getBody().contains("<html>"));
        assertTrue(response.getBody().contains("</html>"));
        // Use actual employeeId from record, e.g. "E01"
        assertTrue(response.getBody().contains("<h2>Payslip for Employee ID: " + record.getEmployeeId() + "</h2>"));
        // Check salary values formatted as string
//        assertTrue(response.getBody().contains(String.valueOf(record.getBasicSalary())));
//        assertTrue(response.getBody().contains(String.format("%.2f", record.getNetSalary())));
        assertTrue(response.getBody().contains(String.valueOf(record.getBasicSalary())));
        assertTrue(response.getBody().contains(String.valueOf(record.getMedicalAllowance())));
        assertTrue(response.getBody().contains(String.valueOf(record.getTransportFee())));
        assertTrue(response.getBody().contains(String.valueOf(record.getSportsFee())));
        assertTrue(response.getBody().contains(String.valueOf(record.getTaxDeduction())));
        assertTrue(response.getBody().contains(String.valueOf(record.getNetSalary())));
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
        request.setEmployeeId("E01");
        request.setType("travel");
        request.setAmount(1500.0);
        request.setDescription("Taxi fare for client meeting");

        ReimbursementResponseDTO mockResponse = new ReimbursementResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setEmployeeId("E01");
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
        String employeeId = "E01";
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
        String employeeId = "E01";
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        // Mock User Service response
        UserDTO user = new UserDTO(employeeId, "ENGINEER");
        when(userServiceClient.getUserDetails(employeeId)).thenReturn(user);

        // Mock Attendance Service response
        AttendanceDTO attendance = new AttendanceDTO(employeeId, month, year, 20, 2, 1);
        when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year))
                .thenReturn(attendance);

        // Prepare request (only needs employeeId now)
        PayrollRequestDTO request = new PayrollRequestDTO();
        request.setEmployeeId(employeeId);
        request.setMonth(month);
        request.setYear(year);

        // Mock approved reimbursements
        ReimbursementRecord reimbursement = createTestReimbursementRecord();
        when(reimbursementRepository.findByEmployeeId(employeeId))
                .thenReturn(List.of(reimbursement));

        // Mock repository save
        when(payrollRepository.save(any(PayrollRecord.class))).thenAnswer(inv -> {
            PayrollRecord r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        // Expected calculations
        RoleSalaryConfig config = RoleSalaryConfig.ENGINEER;
        double basicSalary = config.getBasicSalary();
        double allowances = config.getMedicalAllowance() + config.getOtherAllowance();
        double tax = 0.10 * (basicSalary + allowances);
        double deductions = tax + config.getSportsFee() + config.getTransportFee();
        double noPay = (basicSalary / 20) * attendance.getNotApprovedLeaves();
        double expectedNet = (basicSalary + allowances) - deductions + reimbursement.getAmount() - noPay;

        // Act
        PayrollResponseDTO response = payrollServiceImpl.createPayroll(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(expectedNet, response.getNetSalary(), 0.01);

        // Verify service interactions
        verify(userServiceClient).getUserDetails(employeeId);
        verify(attendanceServiceClient).getAttendanceDetails(employeeId, month, year);
        verify(reimbursementRepository).findByEmployeeId(employeeId);
    }

    @Test
    void deletePayrollsByEmployeeId_Found_ReturnsSuccessMessage() {
        // Arrange
        String employeeId = "E123";
        when(payrollService.deletePayrollsByEmployeeId(employeeId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = payrollDeleteController.deletePayrollsByEmployeeId(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted payroll records for employee " + employeeId, response.getBody());
        verify(payrollService, times(1)).deletePayrollsByEmployeeId(employeeId);
    }

    @Test
    void deletePayrollsByEmployeeId_NotFound_ReturnsNotFound() {
        // Arrange
        String employeeId = "E999";
        when(payrollService.deletePayrollsByEmployeeId(employeeId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = payrollDeleteController.deletePayrollsByEmployeeId(employeeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No payroll records found for employee " + employeeId, response.getBody());
        verify(payrollService, times(1)).deletePayrollsByEmployeeId(employeeId);
    }

    @Test
    void testFeignClient_userDetailsReturnsNull_shouldHandleGracefully() {
        when(userServiceClient.getUserDetails("UNKNOWN_ID")).thenReturn(null);
        UserDTO user = userServiceClient.getUserDetails("UNKNOWN_ID");
        assertNull(user, "User should be null for unknown employee ID");
    }

    @Test
    void testFeignClient_userServiceThrowsException_shouldCatchAndHandle() {
        when(userServiceClient.getUserDetails("E404"))
                .thenThrow(new RetryableException(
                        404,
                        "User not found",
                        Request.HttpMethod.GET,
                        null,
                        Request.create(Request.HttpMethod.GET, "/user/E404", Collections.emptyMap(), null, StandardCharsets.UTF_8, null)
                ));

        assertThrows(RetryableException.class, () -> {
            userServiceClient.getUserDetails("E404");
        });
    }

    @Test
    void testFeignClient_attendanceWithZeroWorkingDays() {
        AttendanceDTO dto = new AttendanceDTO("E200", 5, 2025, 0, 0, 0);
        when(attendanceServiceClient.getAttendanceDetails("E200", 5, 2025)).thenReturn(dto);
        AttendanceDTO result = attendanceServiceClient.getAttendanceDetails("E200", 5, 2025);
        assertNotNull(result);
        assertEquals(0, result.getWorkingDays(), "Working days should be zero");
    }

    @Test
    void testCreatePayrollIntegrationWithFeignClients() {
        String employeeId = "E100";
        int month = 5;
        int year = 2025;

        // Mock Feign clients
        UserDTO user = new UserDTO(employeeId, "ENGINEER");
        when(userServiceClient.getUserDetails(employeeId)).thenReturn(user);

        AttendanceDTO attendance = new AttendanceDTO(employeeId, month, year, 20, 2, 0);
        when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year)).thenReturn(attendance);

        // Mock DB operations
        when(reimbursementRepository.findByEmployeeId(employeeId)).thenReturn(Collections.emptyList());
        when(payrollRepository.save(any())).thenAnswer(inv -> {
            PayrollRecord record = inv.getArgument(0);
            record.setId(1L);
            return record;
        });

        PayrollRequestDTO request = new PayrollRequestDTO();
        request.setEmployeeId(employeeId);
        request.setMonth(month);
        request.setYear(year);
        PayrollResponseDTO response = payrollServiceImpl.createPayroll(request);

        assertNotNull(response);
        assertEquals(employeeId, response.getEmployeeId());
        assertTrue(response.getNetSalary() > 0);
    }




}