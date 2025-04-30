package com.example.PayrollService;

import com.example.PayrollService.controller.PayrollCreationController;
import com.example.PayrollService.controller.PayrollReadController;
import com.example.PayrollService.dto.PayrollRequestDTO;
import com.example.PayrollService.dto.PayrollResponseDTO;
import com.example.PayrollService.service.PayrollService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PayrollServiceApplicationTests {

	@Mock
	private PayrollService payrollService;

	@InjectMocks
	private PayrollCreationController payrollCreationController;

	@InjectMocks
	private PayrollReadController payrollReadController;

	@Test
	void contextLoads() {
	}

	@Test
	void testCreatePayroll() {
		// Setup
		PayrollRequestDTO requestDTO = new PayrollRequestDTO();
		PayrollResponseDTO mockResponse = new PayrollResponseDTO();
		when(payrollService.createPayroll(any(PayrollRequestDTO.class))).thenReturn(mockResponse);

		// Execute
		ResponseEntity<PayrollResponseDTO> response = payrollCreationController.createPayroll(requestDTO);

		// Verify
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(mockResponse, response.getBody());
	}

	@Test
	void testGeneratePayrollsForAll() {
		// Execute
		ResponseEntity<String> response = payrollCreationController.generatePayrollsForAll(null, null);

		// Verify
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Payrolls generated for all employees.", response.getBody());
	}

	@Test
	void testGetPayrollById() {
		// Setup
		PayrollResponseDTO mockResponse = new PayrollResponseDTO();
		when(payrollService.getPayrollById(anyLong())).thenReturn(mockResponse);

		// Execute
		ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

		// Verify
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockResponse, response.getBody());
	}

	@Test
	void testGetPayrollByIdNotFound() {
		// Setup
		when(payrollService.getPayrollById(anyLong())).thenReturn(null);

		// Execute
		ResponseEntity<PayrollResponseDTO> response = payrollReadController.getPayrollById(1L);

		// Verify
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testGetPayrollsByEmployeeId() {
		// Setup
		PayrollResponseDTO payroll1 = new PayrollResponseDTO();
		PayrollResponseDTO payroll2 = new PayrollResponseDTO();
		List<PayrollResponseDTO> mockList = Arrays.asList(payroll1, payroll2);
		when(payrollService.getPayrollsByEmployeeId(anyLong())).thenReturn(mockList);

		// Execute
		ResponseEntity<List<PayrollResponseDTO>> response = payrollReadController.getPayrollsByEmployeeId(1L);

		// Verify
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}

	@Test
	void testGetPayrollsByEmployeeIdNoContent() {
		// Setup
		when(payrollService.getPayrollsByEmployeeId(anyLong())).thenReturn(Collections.emptyList());

		// Execute
		ResponseEntity<List<PayrollResponseDTO>> response = payrollReadController.getPayrollsByEmployeeId(1L);

		// Verify
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void testGetAllPayrolls() {
		// Setup
		PayrollResponseDTO payroll1 = new PayrollResponseDTO();
		PayrollResponseDTO payroll2 = new PayrollResponseDTO();
		List<PayrollResponseDTO> mockList = Arrays.asList(payroll1, payroll2);
		when(payrollService.getAllPayrolls()).thenReturn(mockList);

		// Execute
		ResponseEntity<List<PayrollResponseDTO>> response = payrollReadController.getAllPayrolls();

		// Verify
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}
}