package com.attendance.controller;

import com.attendance.dto.*;
import com.attendance.entity.Attendance;
import com.attendance.entity.Leave;
import com.attendance.exception.ValidationException;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.LeaveRepository;
import com.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final LeaveRepository leaveRepository;

    @Autowired
    public AttendanceController(
            AttendanceService attendanceService,
            AttendanceRepository attendanceRepository,
            LeaveRepository leaveRepository) {
        this.attendanceService = attendanceService;
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
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
            @PathVariable String employeeId,
            @Valid @RequestBody AttendanceRequest request,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Recording attendance for employeeId: {}", employeeId);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
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
            @PathVariable String employeeId,
            @Valid @RequestBody CheckInRequest request,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Recording check-in for employeeId: {}, request: {}", employeeId, request);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(request.getDate());
        attendance.setCheckInTime(request.getCheckInTime());

        Attendance savedAttendance = attendanceService.saveAttendance(attendance);
        logger.debug("Saved attendance: {}", savedAttendance);
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
    public ResponseEntity<List<Attendance>> getAttendanceRecords(
            @PathVariable String employeeId,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving attendance records for employeeId: {}", employeeId);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

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
            @PathVariable String employeeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving total working hours for employeeId: {} from {} to {}", employeeId, startDate, endDate);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

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
            @RequestHeader(value = "employeeId", required = true) String employeeId,
            @Valid @RequestBody LeaveRequest request,
            @RequestAttribute("userId") String tokenUserId) {
        if (employeeId == null) {
            logger.warn("Employee ID header is missing");
            throw new IllegalArgumentException("Employee ID header is required");
        }

        validateEmployeeId(employeeId, tokenUserId);

        logger.info("Processing leave request for employee ID: {}", employeeId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
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
            @PathVariable String employeeId,
            @RequestParam("month") String month,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving attendance count for employeeId: {} for month: {}", employeeId, month);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        if (!month.matches("\\d{4}-(0[1-9]|1[0-2])")) {
            logger.warn("Invalid month format or value: {}", month);
            throw new IllegalArgumentException("Month must be in 'yyyy-MM' format with a valid month (1-12)");
        }

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

    @Operation(summary = "Get the remaining leave balance for an employee in a specific month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave balance retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/employee/{employeeId}/leave-balance")
    public ResponseEntity<LeaveBalanceResponse> getLeaveBalance(
            @PathVariable String employeeId,
            @RequestParam("month") String month,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving leave balance for employeeId: {} for month: {}", employeeId, month);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        if (!month.matches("\\d{4}-(0[1-9]|1[0-2])")) {
            logger.warn("Invalid month format or value: {}", month);
            throw new IllegalArgumentException("Month must be in 'yyyy-MM' format with a valid month (1-12)");
        }

        LocalDate monthDate;
        try {
            monthDate = LocalDate.parse(month + "-01", DATE_FORMATTER);
        } catch (Exception e) {
            logger.warn("Failed to parse month: {} due to {}", month, e.getMessage());
            throw new IllegalArgumentException("Invalid month format: " + month + ". Use 'yyyy-MM' with a valid month (1-12)");
        }

        LeaveBalanceResponse leaveBalance = attendanceService.getLeaveBalance(employeeId, monthDate);
        return ResponseEntity.ok(leaveBalance);
    }

    @Operation(summary = "Delete a leave request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Leave request deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, leave not found, or leave not in PENDING status"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/leaves/{leaveId}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long leaveId) {
        logger.info("Processing delete request for leaveId: {}", leaveId);

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.warn("Leave request with ID {} not found", leaveId);
                    return new IllegalArgumentException("Leave request not found for leaveId: " + leaveId);
                });

        attendanceService.deleteLeaveRequest(leaveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Approve or reject a leave request by its ID (Admin-only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, leave not found, or leave not in PENDING status"),
            @ApiResponse(responseCode = "403", description = "Unauthorized: Only Admins can approve/reject leaves"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/leaves/{leaveId}/status")
    public ResponseEntity<Leave> updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestParam("status") String status,
            @RequestHeader(value = "employeeId", required = true) String requestingEmployeeId) {
        logger.info("Processing leave status update for leaveId: {} to status: {} by employeeId: {}",
                leaveId, status, requestingEmployeeId);

        if (requestingEmployeeId == null) {
            logger.warn("Employee ID header is missing");
            throw new IllegalArgumentException("Employee ID header is required");
        }

        if (!requestingEmployeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", requestingEmployeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.warn("Leave request with ID {} not found", leaveId);
                    return new IllegalArgumentException("Leave request not found for leaveId: " + leaveId);
                });

        Leave updatedLeave = attendanceService.updateLeaveStatus(leaveId, status, requestingEmployeeId);
        return ResponseEntity.ok(updatedLeave);
    }

    @Operation(summary = "Get leave requests for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No leave requests found"),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/leaves/employee/{employeeId}")
    public ResponseEntity<List<Leave>> getLeaveRequests(
            @PathVariable String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving leave requests for employeeId: {}, startDate: {}, endDate: {}", employeeId, startDate, endDate);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        List<Leave> leaveRequests;
        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                logger.warn("endDate {} is before startDate {}", endDate, startDate);
                throw new IllegalArgumentException("endDate must be after or equal to startDate");
            }
            leaveRequests = leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
        } else {
            leaveRequests = leaveRepository.findByEmployeeId(employeeId);
        }

        if (leaveRequests.isEmpty()) {
            logger.warn("No leave requests found for employeeId: {}", employeeId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequests);
    }

    @Operation(summary = "Get monthly attendance and leave details for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{employeeId}/details")
    public ResponseEntity<AttendanceSummaryResponse> getAttendanceDetails(
            @PathVariable String employeeId,
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            @RequestAttribute("userId") String tokenUserId) {
        logger.info("Retrieving attendance details for employeeId: {}, month: {}, year: {}", employeeId, month, year);

        validateEmployeeId(employeeId, tokenUserId);

        if (!employeeId.matches("^[ESM]\\d{3}$")) {
            logger.warn("Invalid employeeId format: {}. Expected format: [ESM]ddd (e.g., E001, S001, M001)", employeeId);
            throw new IllegalArgumentException("Invalid employeeId format. Use [ESM]ddd (e.g., E001, S001, M001)");
        }

        if (month < 1 || month > 12) {
            logger.warn("Invalid month value: {}. Month must be between 1 and 12", month);
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        if (year < 2000 || year > 2100) {
            logger.warn("Invalid year value: {}. Year must be between 2000 and 2100", year);
            throw new IllegalArgumentException("Year must be between 2000 and 2100");
        }

        AttendanceSummaryResponse details = attendanceService.getAttendanceDetails(employeeId, month, year);
        return ResponseEntity.ok(details);
    }

    private void validateEmployeeId(String employeeId, String tokenUserId) {
        if (tokenUserId != null && !employeeId.equals(tokenUserId)) {
            logger.warn("Access denied: Employee ID {} does not match token user ID {}", employeeId, tokenUserId);
            throw new IllegalArgumentException("Access denied: Employee ID in request does not match token user ID");
        }
    }
}