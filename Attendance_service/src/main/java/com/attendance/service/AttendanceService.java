package com.attendance.service;

import com.attendance.dto.LeaveBalanceResponse;
import com.attendance.entity.Attendance;
import com.attendance.entity.Leave;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.LeaveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;

    private static final long MAX_ANNUAL_DAYS = 14;
    private static final long MAX_CASUAL_DAYS = 7;
    private static final long MAX_HALF_DAYS = 2;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, LeaveRepository leaveRepository) {
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public double calculateWorkingHours(Long recordId) {
        logger.info("Calculating working hours for recordId: {}", recordId);
        Attendance attendance = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found for recordId: " + recordId));

        LocalTime checkIn = attendance.getCheckInTime();
        LocalTime checkOut = attendance.getCheckOutTime();

        if (checkIn == null || checkOut == null) {
            logger.warn("Check-in or check-out time is missing for recordId: {}", recordId);
            throw new IllegalStateException("Cannot calculate working hours: Check-in or check-out time is missing");
        }

        Duration duration = Duration.between(checkIn, checkOut);
        double hours = duration.toMinutes() / 60.0;
        logger.info("Calculated working hours for recordId {}: {}", recordId, hours);
        return hours;
    }

    public double calculateTotalWorkingHours(String employeeId, LocalDate startDate, LocalDate endDate) { // Changed from Long to String
        logger.info("Calculating total working hours for employeeId: {} from {} to {}", employeeId, startDate, endDate);

        if (startDate.isAfter(endDate)) {
            logger.warn("Invalid date range: startDate {} is after endDate {}", startDate, endDate);
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        List<Attendance> records = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate); // Already String in repository
        if (records.isEmpty()) {
            logger.warn("No attendance records found for employeeId: {} between {} and {}", employeeId, startDate, endDate);
            return 0.0;
        }

        double totalHours = 0.0;
        for (Attendance record : records) {
            try {
                LocalTime checkIn = record.getCheckInTime();
                LocalTime checkOut = record.getCheckOutTime();

                if (checkIn == null || checkOut == null) {
                    logger.warn("Skipping recordId {}: Check-in or check-out time is missing", record.getId());
                    continue;
                }

                Duration duration = Duration.between(checkIn, checkOut);
                double hours = duration.toMinutes() / 60.0;
                totalHours += hours;
                logger.debug("RecordId {}: {} hours", record.getId(), hours);
            } catch (Exception e) {
                logger.error("Error calculating hours for recordId {}: {}", record.getId(), e.getMessage());
            }
        }

        logger.info("Total working hours for employeeId {}: {}", employeeId, totalHours);
        return totalHours;
    }

    public Leave saveLeaveRequest(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String leaveType) { // Changed from Long to String
        logger.info("Saving leave request for employeeId: {} from {} to {} with leaveType: {}",
                employeeId, startDate, endDate, leaveType);

        if (endDate.isBefore(startDate)) {
            logger.warn("Invalid date range: endDate {} is before startDate {}", endDate, startDate);
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        String normalizedLeaveType = leaveType.trim().toUpperCase();
        if (!normalizedLeaveType.equals("ANNUAL") && !normalizedLeaveType.equals("CASUAL") && !normalizedLeaveType.equals("HALF DAY")) {
            logger.warn("Invalid leave type: {}", leaveType);
            throw new IllegalArgumentException("Leave type must be 'Annual', 'Casual', or 'Half Day'");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        LocalDate monthStart = startDate.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<Leave> existingLeaves = leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, monthStart, monthEnd); // Already String in repository

        long annualDays = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("ANNUAL") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long casualDays = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("CASUAL") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long halfDayCount = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("HALF DAY") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .count();

        if (normalizedLeaveType.equals("ANNUAL") && annualDays + daysBetween > MAX_ANNUAL_DAYS) {
            logger.warn("Annual leave limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 14 annual leave days allowed per month");
        }
        if (normalizedLeaveType.equals("CASUAL") && casualDays + daysBetween > MAX_CASUAL_DAYS) {
            logger.warn("Casual leave limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 7 casual leave days allowed per month");
        }
        if (normalizedLeaveType.equals("HALF DAY") && halfDayCount + 1 > MAX_HALF_DAYS) {
            logger.warn("Half day limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 2 half days allowed per month");
        }

        Leave leave = new Leave(employeeId, startDate, endDate, reason, "PENDING", normalizedLeaveType); // Already String in constructor
        Leave savedLeave = leaveRepository.save(leave);
        logger.info("Leave request saved successfully for employeeId: {}", employeeId);
        return savedLeave;
    }

    public long getAttendanceCountPerMonth(String employeeId, LocalDate month) { // Changed from Long to String
        logger.info("Calculating attendance count for employeeId: {} for month: {}", employeeId, month);

        LocalDate monthStart = month.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        List<Attendance> records = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, monthStart, monthEnd); // Already String in repository

        long attendedDays = records.stream()
                .filter(record -> record.getCheckInTime() != null && record.getCheckOutTime() != null)
                .map(Attendance::getDate)
                .distinct()
                .count();

        logger.info("Attendance count for employeeId {} in month {}: {}", employeeId, month, attendedDays);
        return attendedDays;
    }

    public LeaveBalanceResponse getLeaveBalance(String employeeId, LocalDate month) { // Changed from Long to String
        logger.info("Calculating leave balance for employeeId: {} for month: {}", employeeId, month);

        LocalDate monthStart = month.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        List<Leave> leaves = leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, monthStart, monthEnd); // Already String in repository

        long usedAnnualDays = leaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("ANNUAL") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long usedCasualDays = leaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("CASUAL") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long usedHalfDays = leaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("HALF DAY") && l.getStatus().equalsIgnoreCase("APPROVED"))
                .count();

        long remainingAnnualDays = MAX_ANNUAL_DAYS - usedAnnualDays;
        long remainingCasualDays = MAX_CASUAL_DAYS - usedCasualDays;
        long remainingHalfDays = MAX_HALF_DAYS - usedHalfDays;

        logger.info("Leave balance for employeeId {} in month {}: Annual={}, Casual={}, HalfDay={}",
                employeeId, month, remainingAnnualDays, remainingCasualDays, remainingHalfDays);

        return new LeaveBalanceResponse(remainingAnnualDays, remainingCasualDays, remainingHalfDays);
    }

    public void deleteLeaveRequest(Long leaveId) {
        logger.info("Attempting to delete leave request with leaveId: {}", leaveId);

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.warn("Leave request with ID {} not found", leaveId);
                    return new IllegalArgumentException("Leave request not found for leaveId: " + leaveId);
                });

        if (!leave.getStatus().equalsIgnoreCase("PENDING")) {
            logger.warn("Cannot delete leave request with leaveId: {} because its status is {}", leaveId, leave.getStatus());
            throw new IllegalStateException("Cannot delete leave request: It must be in PENDING status");
        }

        leaveRepository.delete(leave);
        logger.info("Leave request with leaveId: {} deleted successfully", leaveId);
    }

    public Leave updateLeaveStatus(Long leaveId, String newStatus, String employeeId) { // Changed from Long to String
        logger.info("Attempting to update leave status for leaveId: {} to {} by employeeId: {}", leaveId, newStatus, employeeId);

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.warn("Leave request with ID {} not found", leaveId);
                    return new IllegalArgumentException("Leave request not found for leaveId: " + leaveId);
                });

        if (!leave.getStatus().equalsIgnoreCase("PENDING")) {
            logger.warn("Cannot update leave status for leaveId: {} because its current status is {}", leaveId, leave.getStatus());
            throw new IllegalStateException("Cannot update leave status: It must be in PENDING status");
        }

        if (!leave.getEmployeeId().equals(employeeId)) { // Compare String with String
            logger.warn("EmployeeId {} is not authorized to update leaveId: {}", employeeId, leaveId);
            throw new IllegalStateException("Unauthorized: Only the requesting employee can update the leave");
        }

        String normalizedStatus = newStatus.trim().toUpperCase();
        if (!normalizedStatus.equals("APPROVED") && !normalizedStatus.equals("REJECTED")) {
            logger.warn("Invalid status provided: {}", newStatus);
            throw new IllegalArgumentException("Status must be 'APPROVED' or 'REJECTED'");
        }

        leave.setStatus(normalizedStatus);
        Leave updatedLeave = leaveRepository.save(leave);
        logger.info("Leave status for leaveId: {} updated to {} by employeeId: {}", leaveId, normalizedStatus, employeeId);
        return updatedLeave;
    }
}