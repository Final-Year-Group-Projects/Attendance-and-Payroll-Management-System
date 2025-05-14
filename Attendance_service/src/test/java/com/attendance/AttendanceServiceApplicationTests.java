package com.attendance;

import com.attendance.controller.AttendanceController;
import com.attendance.dto.AttendanceRequest;
import com.attendance.dto.LeaveRequest;
import com.attendance.entity.Attendance;
import com.attendance.entity.Leave;
import com.attendance.repository.LeaveRepository;
import com.attendance.service.AttendanceService;
import com.attendance.client.UserServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AttendanceServiceApplicationTests {

	private MockMvc mockMvc;

	@Mock
	private AttendanceService attendanceService;

	@Mock
	private UserServiceClient userServiceClient;

	@Mock
	private LeaveRepository leaveRepository;

	@InjectMocks
	private AttendanceController attendanceController;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		// Configure ObjectMapper with custom LocalDate serialization
		objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		objectMapper.registerModule(javaTimeModule);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// Configure MockMvc to use the custom ObjectMapper for response serialization
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);

		// Add custom exception handler for IllegalArgumentException
		mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
				.setMessageConverters(converter)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	// Custom exception handler for testing
	@RestControllerAdvice
	static class GlobalExceptionHandler {
		@ExceptionHandler(IllegalArgumentException.class)
		public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@Test
	void testRecordAttendance() throws Exception {
		// Mock for POST /attendance/1
		Attendance attendance = new Attendance();
		attendance.setEmployeeId(1L);
		attendance.setDate(LocalDate.of(2025, 5, 14));
		attendance.setCheckInTime(LocalTime.of(9, 0));
		attendance.setCheckOutTime(LocalTime.of(17, 0));
		when(attendanceService.saveAttendance(any(Attendance.class))).thenReturn(attendance);

		AttendanceRequest request = new AttendanceRequest();
		request.setDate("2025-05-14");
		request.setCheckInTime("09:00:00");
		request.setCheckOutTime("17:00:00");

		mockMvc.perform(post("/attendance/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeId").value(1))
				.andExpect(jsonPath("$.date").value("2025-05-14"))
				.andExpect(jsonPath("$.checkInTime").value("09:00:00"))
				.andExpect(jsonPath("$.checkOutTime").value("17:00:00"));
	}

	@Test
	void testRequestLeave() throws Exception {
		// Mock for POST /attendance/leaves/request
		Leave leave = new Leave();
		leave.setId(5L);
		leave.setEmployeeId(1L);
		leave.setStartDate(LocalDate.of(2025, 5, 15));
		leave.setEndDate(LocalDate.of(2025, 5, 15));
		leave.setReason("Test leave");
		leave.setStatus("PENDING");
		leave.setLeaveType("CASUAL");
		when(attendanceService.saveLeaveRequest(eq(1L), eq(LocalDate.of(2025, 5, 15)), eq(LocalDate.of(2025, 5, 15)), eq("Test leave"), eq("CASUAL"))).thenReturn(leave);

		LeaveRequest request = new LeaveRequest();
		request.setStartDate(LocalDate.of(2025, 5, 15));
		request.setEndDate(LocalDate.of(2025, 5, 15));
		request.setReason("Test leave");
		request.setLeaveType("CASUAL");

		mockMvc.perform(post("/attendance/leaves/request")
						.contentType(MediaType.APPLICATION_JSON)
						.header("employeeId", "1")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.employeeId").value(1))
				.andExpect(jsonPath("$.startDate").value("2025-05-15"))
				.andExpect(jsonPath("$.endDate").value("2025-05-15"))
				.andExpect(jsonPath("$.reason").value("Test leave"))
				.andExpect(jsonPath("$.status").value("PENDING"))
				.andExpect(jsonPath("$.leaveType").value("CASUAL"));
	}

	@Test
	void testApproveLeave() throws Exception {
		// Mock for PUT /attendance/leaves/5/status
		Leave leave = new Leave();
		leave.setId(5L);
		leave.setEmployeeId(1L);
		leave.setStartDate(LocalDate.of(2025, 5, 15));
		leave.setEndDate(LocalDate.of(2025, 5, 15));
		leave.setReason("Test leave");
		leave.setStatus("PENDING");
		leave.setLeaveType("CASUAL");

		Leave updatedLeave = new Leave();
		updatedLeave.setId(5L);
		updatedLeave.setEmployeeId(1L);
		updatedLeave.setStartDate(LocalDate.of(2025, 5, 15));
		updatedLeave.setEndDate(LocalDate.of(2025, 5, 15));
		updatedLeave.setReason("Test leave");
		updatedLeave.setStatus("APPROVED");
		updatedLeave.setLeaveType("CASUAL");

		when(leaveRepository.findById(eq(5L))).thenReturn(java.util.Optional.of(leave));
		when(attendanceService.updateLeaveStatus(eq(5L), eq("APPROVED"), eq(2L))).thenReturn(updatedLeave);

		mockMvc.perform(put("/attendance/leaves/5/status")
						.param("status", "APPROVED")
						.header("employeeId", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("APPROVED"))
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.employeeId").value(1))
				.andExpect(jsonPath("$.startDate").value("2025-05-15"))
				.andExpect(jsonPath("$.endDate").value("2025-05-15"))
				.andExpect(jsonPath("$.reason").value("Test leave"))
				.andExpect(jsonPath("$.leaveType").value("CASUAL"));
	}

	@Test
	void testGetLeaveRequestsWithNoDateRange() throws Exception {
		// Mock LeaveRepository for all leaves
		Leave leave = new Leave();
		leave.setId(1L);
		leave.setEmployeeId(1L);
		leave.setStartDate(LocalDate.of(2025, 5, 15));
		leave.setEndDate(LocalDate.of(2025, 5, 15));
		leave.setReason("Vacation");
		leave.setStatus("APPROVED");
		leave.setLeaveType("ANNUAL");
		List<Leave> leaveList = Collections.singletonList(leave);
		when(leaveRepository.findByEmployeeId(1L)).thenReturn(leaveList);

		// Perform GET request
		mockMvc.perform(get("/attendance/leaves/employee/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].employeeId").value(1))
				.andExpect(jsonPath("$[0].startDate").value("2025-05-15"))
				.andExpect(jsonPath("$[0].endDate").value("2025-05-15"))
				.andExpect(jsonPath("$[0].reason").value("Vacation"))
				.andExpect(jsonPath("$[0].status").value("APPROVED"))
				.andExpect(jsonPath("$[0].leaveType").value("ANNUAL"));
	}

	@Test
	void testGetLeaveRequestsWithDateRange() throws Exception {
		// Mock LeaveRepository for date range
		Leave leave = new Leave();
		leave.setId(1L);
		leave.setEmployeeId(1L);
		leave.setStartDate(LocalDate.of(2025, 5, 15));
		leave.setEndDate(LocalDate.of(2025, 5, 15));
		leave.setReason("Vacation");
		leave.setStatus("APPROVED");
		leave.setLeaveType("ANNUAL");
		List<Leave> leaveList = Collections.singletonList(leave);
		when(leaveRepository.findByEmployeeIdAndStartDateBetween(1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 31)))
				.thenReturn(leaveList);

		// Perform GET request with date range
		mockMvc.perform(get("/attendance/leaves/employee/1")
						.contentType(MediaType.APPLICATION_JSON)
						.param("startDate", "2025-05-01")
						.param("endDate", "2025-05-31"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].employeeId").value(1))
				.andExpect(jsonPath("$[0].startDate").value("2025-05-15"))
				.andExpect(jsonPath("$[0].endDate").value("2025-05-15"))
				.andExpect(jsonPath("$[0].reason").value("Vacation"))
				.andExpect(jsonPath("$[0].status").value("APPROVED"))
				.andExpect(jsonPath("$[0].leaveType").value("ANNUAL"));
	}

	@Test
	void testGetLeaveRequestsNoContent() throws Exception {
		// Mock LeaveRepository with empty list
		when(leaveRepository.findByEmployeeId(1L)).thenReturn(Collections.emptyList());

		// Perform GET request
		mockMvc.perform(get("/attendance/leaves/employee/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testGetLeaveRequestsEmployeeNotFound() throws Exception {
		// Perform GET request with invalid employeeId
		mockMvc.perform(get("/attendance/leaves/employee/999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}