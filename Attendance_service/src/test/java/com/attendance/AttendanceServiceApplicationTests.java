package com.attendance;

import com.attendance.controller.AttendanceController;
import com.attendance.controller.AttendanceRequest;
import com.attendance.entity.Attendance;
import com.attendance.service.AttendanceService;
import com.attendance.client.UserServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceApplicationTests {

	private MockMvc mockMvc;

	@Mock
	private AttendanceService attendanceService;

	@Mock
	private UserServiceClient userServiceClient;

	@InjectMocks
	private AttendanceController attendanceController;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();

		Attendance attendance = new Attendance();
		attendance.setEmployeeId(1L);
		attendance.setDate(LocalDate.of(2025, 4, 17));
		attendance.setCheckInTime(LocalTime.of(9, 0));
		attendance.setCheckOutTime(LocalTime.of(17, 0));

		when(attendanceService.saveAttendance(any(Attendance.class))).thenReturn(attendance);
	}

	@Test
	void testRecordAttendance() throws Exception {
		AttendanceRequest request = new AttendanceRequest();
		request.setDate("2025-04-17");
		request.setCheckInTime("09:00");
		request.setCheckOutTime("17:00");

		mockMvc.perform(post("/attendance/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}
}