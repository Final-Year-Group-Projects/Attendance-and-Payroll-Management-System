package com.attendance.controller;

import com.attendance.client.UserServiceClient;
import com.attendance.dto.AttendanceRequest;
import com.attendance.dto.CheckInRequest;
import com.attendance.dto.CheckOutRequest;
import com.attendance.dto.LeaveRequest;
import com.attendance.dto.WorkingHoursResponse;
import com.attendance.dto.TotalWorkingHoursResponse;
import com.attendance.dto.AttendanceCountResponse;
import com.attendance.entity.Attendance;
import com.attendance.entity.Leave;
import com.attendance.exception.ValidationException;
import com.attendance.repository.AttendanceRepository;
import com.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public AttendanceController(
            AttendanceService attendanceService,
            AttendanceRepository attendanceRepository,
            UserServiceClient userServiceClient) {
        this.attendanceService = attendanceService;
        this.attendanceRepository = attendanceRepository;
        this.userServiceClient = userServiceClient;
        logger.info("AttendanceController initialized");
    }

    @Operation(summary = "Record attendance for an employee (check-in and check-out) - Legacy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/{employeeId}")
    public ResponseEntity<Attendance> recordAttendance(
            @PathVariable Long employeeId,
            @Valid @RequestBody AttendanceRequest request) {
        logger.info("Recording attendance for employeeId: {}", employeeId);

        // Validate employee
        UserServiceClient.EmployeeDTO employee = userServiceClient.getUserById(employeeId);
        if (employee == null) {
            logger.warn("Employee with ID {} not found", employeeId);
            throw new IllegalArgumentException("Employee not found");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(LocalDate.parse(request.getDate(), DATE_FORMATTER));
        attendance.setCheckInTime(LocalTime.parse(request.getCheckInTime(), TIME_FORMATTER));
        attendance.setCheckOutTime(LocalTime.parse(request.getCheckOutTime(), TIME_FORMATTER));

        Attendance savedAttendance = attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok(savedAttendance);
    }

    @Operation(summary = "Record check-in for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-in recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<Attendance> checkIn(
            @PathVariable Long employeeId,
            @Valid @RequestBody CheckInRequest request) {
        logger.info("Recording check-in for employeeId: {}", employeeId);

        // Validate employee
        UserServiceClient.EmployeeDTO employee = userServiceClient.getUserById(employeeId);
        if (employee == null) {
            logger.warn("Employee with ID {} not found", employeeId);
            throw new IllegalArgumentException("Employee not found");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(LocalDate.parse(request.getDate(), DATE_FORMATTER));
        attendance.setCheckInTime(LocalTime.parse(request.getCheckInTime(), TIME_FORMATTER));

        Attendance savedAttendance = attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok(savedAttendance);
    }

    @Operation(summary = "Record check-out for an attendance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-out recorded successfully"),
            @ApiResponse(responseCode = "404", description = "Attendance record not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input or check-out time is not after check-in time"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/check-out/{recordId}")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Long recordId,
            @Valid @RequestBody CheckOutRequest request) {
        logger.info("Recording check-out for recordId: {}", recordId);

        Attendance attendance = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found for recordId: " + recordId));

        LocalTime checkInTime = attendance.getCheckInTime();
        if (checkInTime == null) {
            logger.warn("Check-in time is missing for recordId: {}", recordId);
            throw new ValidationException("Cannot record check-out: Check-in time is missing for recordId: " + recordId);
        }

        LocalTime checkOutTime = LocalTime.parse(request.getCheckOutTime(), TIME_FORMATTER);
        if (!checkOutTime.isAfter(checkInTime)) {
            logger.warn("Check-out time {} is not after check-in time {} for recordId: {}",
                    checkOutTime.format(TIME_FORMATTER),
                    checkInTime.format(TIME_FORMATTER),
                    recordId);
            throw new ValidationException("Check-out time must be after check-in time");
        }

        attendance.setCheckOutTime(checkOutTime);
        Attendance updatedAttendance = attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok(updatedAttendance);
    }

    @Operation(summary = "Retrieve attendance records for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No records found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Attendance>> getAttendanceRecords(@PathVariable Long employeeId) {
        logger.info("Retrieving attendance records for employeeId: {}", employeeId);
        List<Attendance> records = attendanceRepository.findByEmployeeId(employeeId);
        if (records.isEmpty()) {
            logger.warn("No attendance records found for employeeId: {}", employeeId);
            throw new IllegalArgumentException("No attendance records found for employeeId: " + employeeId);
        }
        return ResponseEntity.ok(records);
    }

    @Operation(summary = "Calculate working hours for an attendance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Working hours calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Attendance record not found"),
            @ApiResponse(responseCode = "400", description = "Cannot calculate hours due to missing check-in or check-out"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/hours/{recordId}")
    public ResponseEntity<WorkingHoursResponse> getWorkingHours(@PathVariable Long recordId) {
        logger.info("Retrieving working hours for recordId: {}", recordId);
        double hours = attendanceService.calculateWorkingHours(recordId);
        return ResponseEntity.ok(new WorkingHoursResponse(hours));
    }

    @Operation(summary = "Calculate total working hours for an employee over a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total working hours calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/employee/{employeeId}/hours")
    public ResponseEntity<TotalWorkingHoursResponse> getTotalWorkingHours(
            @PathVariable Long employeeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        logger.info("Retrieving total working hours for employeeId: {} from {} to {}", employeeId, startDate, endDate);

        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);

        double totalHours = attendanceService.calculateTotalWorkingHours(employeeId, start, end);
        return ResponseEntity.ok(new TotalWorkingHoursResponse(totalHours));
    }

    @Operation(summary = "Submit a leave request for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/leaves/request")
    public ResponseEntity<Leave> requestLeave(
            @RequestHeader(value = "employeeId", required = true) Long employeeId,
            @Valid @RequestBody LeaveRequest request) {
        if (employeeId == null) {
            logger.warn("Employee ID header is missing");
            throw new IllegalArgumentException("Employee ID header is required");
        }

        logger.info("Processing leave request for employee ID: {}", employeeId);

        // Validate employee
        UserServiceClient.EmployeeDTO employee = userServiceClient.getUserById(employeeId);
        if (employee == null) {
            logger.warn("Employee with ID {} not found", employeeId);
            throw new IllegalArgumentException("Employee not found");
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        Leave savedLeave = attendanceService.saveLeaveRequest(
                employeeId,
                startDate,
                endDate,
                request.getReason(),
                request.getLeaveType()
        );
        logger.info("Leave request submitted successfully for employee ID: {}", employeeId);
        return ResponseEntity.ok(savedLeave);
    }

    @Operation(summary = "Get the number of attended days for an employee in a specific month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance count retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/employee/{employeeId}/attendance-count")
    public ResponseEntity<AttendanceCountResponse> getAttendanceCount(
            @PathVariable Long employeeId,
            @RequestParam("month") String month) { // Format: "yyyy-MM"
        logger.info("Retrieving attendance count for employeeId: {} for month: {}", employeeId, month);

        // Validate employee
        UserServiceClient.EmployeeDTO employee = userServiceClient.getUserById(employeeId);
        if (employee == null) {
            logger.warn("Employee with ID {} not found", employeeId);
            throw new IllegalArgumentException("Employee not found");
        }

        // Validate and parse the month
        if (!month.matches("\\d{4}-(0[1-9]|1[0-2])")) {
            logger.warn("Invalid month format or value: {}", month);
            throw new IllegalArgumentException("Month must be in 'yyyy-MM' format with a valid month (1-12)");
        }

        // Parse the month (e.g., "2025-05") as the first day of the month
        LocalDate monthDate;
        try {
            monthDate = LocalDate.parse(month + "-01", DATE_FORMATTER);
        } catch (Exception e) {
            logger.warn("Failed to parse month: {} due to {}", month, e.getMessage());
            throw new IllegalArgumentException("Invalid month format: " + month + ". Use 'yyyy-MM' with a valid month (1-12)");
        }

        long attendedDays = attendanceService.getAttendanceCountPerMonth(employeeId, monthDate);
        return ResponseEntity.ok(new AttendanceCountResponse(attendedDays));
    }
}