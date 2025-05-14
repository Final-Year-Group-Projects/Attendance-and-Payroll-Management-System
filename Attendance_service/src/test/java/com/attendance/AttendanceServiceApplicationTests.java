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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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

		mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
				.setMessageConverters(converter)
				.build();
	}

	@Test
	void testRecordAttendance() throws Exception {
		// Mock UserServiceClient for employee
		UserServiceClient.EmployeeDTO employee = new UserServiceClient.EmployeeDTO();
		employee.setId(1L);
		employee.setFirstName("Test");
		employee.setLastName("User");
		employee.setEmail("test@example.com");
		employee.setRole("Employee");
		when(userServiceClient.getUserById(1L)).thenReturn(employee);

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
		// Mock UserServiceClient for employee
		UserServiceClient.EmployeeDTO employee = new UserServiceClient.EmployeeDTO();
		employee.setId(1L);
		employee.setFirstName("Test");
		employee.setLastName("User");
		employee.setEmail("test@example.com");
		employee.setRole("Employee");
		when(userServiceClient.getUserById(1L)).thenReturn(employee);

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
		// Mock UserServiceClient for employee
		UserServiceClient.EmployeeDTO employee = new UserServiceClient.EmployeeDTO();
		employee.setId(1L);
		employee.setFirstName("Test");
		employee.setLastName("User");
		employee.setEmail("test@example.com");
		employee.setRole("Employee");
		when(userServiceClient.getUserById(1L)).thenReturn(employee);

		// Mock UserServiceClient for admin
		UserServiceClient.EmployeeDTO admin = new UserServiceClient.EmployeeDTO();
		admin.setId(2L);
		admin.setFirstName("Admin");
		admin.setLastName("User");
		admin.setEmail("admin@example.com");
		admin.setRole("Admin");
		when(userServiceClient.getUserById(2L)).thenReturn(admin);

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
						.header("employeeId", "2")) // Admin ID
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("APPROVED"))
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.employeeId").value(1))
				.andExpect(jsonPath("$.startDate").value("2025-05-15"))
				.andExpect(jsonPath("$.endDate").value("2025-05-15"))
				.andExpect(jsonPath("$.reason").value("Test leave"))
				.andExpect(jsonPath("$.leaveType").value("CASUAL"));
	}
}