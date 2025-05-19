package com.example.PayrollService;

import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.dto.integration.AttendanceDTO;
import com.example.PayrollService.dto.integration.UserDTO;
import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.feign.AttendanceServiceClient;
import com.example.PayrollService.feign.UserServiceClient;
import com.example.PayrollService.repository.PayrollRepository;
import com.example.PayrollService.repository.ReimbursementRepository;
import com.example.PayrollService.service.PayrollServiceImpl;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeignClientIntegrationTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private AttendanceServiceClient attendanceServiceClient;

    @Mock
    private ReimbursementRepository reimbursementRepository;

    @Mock
    private PayrollRepository payrollRepository;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    @Test
    void testFeignClients_returnMockedDataFromFeignTestConfig() {
        when(userServiceClient.getUserDetails("E100"))
                .thenReturn(new UserDTO("E100", "ENGINEER"));

        when(attendanceServiceClient.getAttendanceDetails("E100", 5, 2025))
                .thenReturn(new AttendanceDTO("E100", 5, 2025, 20, 2, 1));

        UserDTO user = userServiceClient.getUserDetails("E100");
        AttendanceDTO attendance = attendanceServiceClient.getAttendanceDetails("E100", 5, 2025);

        assertNotNull(user);
        assertEquals("ENGINEER", user.getRole());

        assertNotNull(attendance);
        assertEquals(20, attendance.getWorkingDays());
    }


    @Test
    void testFeignClient_userDetailsReturnsNull_shouldHandleGracefully() {
        UserServiceClient mockClient = mock(UserServiceClient.class);
        when(mockClient.getUserDetails("UNKNOWN_ID")).thenReturn(null);

        UserDTO user = mockClient.getUserDetails("UNKNOWN_ID");
        assertNull(user);
    }

    @Test
    void testFeignClient_userServiceThrowsException_shouldCatchAndHandle() {
        UserServiceClient mockClient = mock(UserServiceClient.class);
        when(mockClient.getUserDetails("E404")).thenThrow(new RetryableException(
                404,
                "User not found",
                Request.HttpMethod.GET,
                null,
                Request.create(Request.HttpMethod.GET, "/user/E404", Collections.emptyMap(), null, StandardCharsets.UTF_8, null)
        ));

        assertThrows(RetryableException.class, () -> mockClient.getUserDetails("E404"));
    }

    @Test
    void testFeignClient_attendanceWithZeroWorkingDays() {
        AttendanceServiceClient mockAttendanceClient = mock(AttendanceServiceClient.class);
        AttendanceDTO dto = new AttendanceDTO("E200", 5, 2025, 0, 0, 0);
        when(mockAttendanceClient.getAttendanceDetails("E200", 5, 2025)).thenReturn(dto);

        AttendanceDTO result = mockAttendanceClient.getAttendanceDetails("E200", 5, 2025);
        assertNotNull(result);
        assertEquals(0, result.getWorkingDays());
    }

    @Test
    void testCreatePayrollIntegrationWithFeignClients() {
        String employeeId = "E100";
        int month = 5;
        int year = 2025;

        when(userServiceClient.getUserDetails(employeeId))
                .thenReturn(new UserDTO(employeeId, "ENGINEER"));
        when(attendanceServiceClient.getAttendanceDetails(employeeId, month, year))
                .thenReturn(new AttendanceDTO(employeeId, month, year, 20, 2, 1));

        PayrollRepository payrollRepository = mock(PayrollRepository.class);
        ReimbursementRepository reimbursementRepository = mock(ReimbursementRepository.class);

        when(reimbursementRepository.findByEmployeeId(employeeId)).thenReturn(Collections.emptyList());
        when(payrollRepository.save(any(PayrollRecord.class))).thenAnswer(inv -> {
            PayrollRecord record = inv.getArgument(0);
            record.setId(1L);
            return record;
        });

        PayrollServiceImpl payrollService = new PayrollServiceImpl(
                userServiceClient,
                attendanceServiceClient,
                payrollRepository,
                reimbursementRepository

        );

        PayrollRequestDTO request = new PayrollRequestDTO();
        request.setEmployeeId(employeeId);
        request.setMonth(month);
        request.setYear(year);

        PayrollResponseDTO response = payrollService.createPayroll(request);

        assertNotNull(response);
        assertEquals(employeeId, response.getEmployeeId());
        assertTrue(response.getNetSalary() > 0);
    }
}
