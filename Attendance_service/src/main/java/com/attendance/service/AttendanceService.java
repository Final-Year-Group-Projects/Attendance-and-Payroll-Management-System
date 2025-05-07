package com.attendance.service;

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
        double hours = duration.toMinutes() / 60.0; // Convert to hours as a decimal
        logger.info("Calculated working hours for recordId {}: {}", recordId, hours);
        return hours;
    }

    public double calculateTotalWorkingHours(Long employeeId, LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating total working hours for employeeId: {} from {} to {}", employeeId, startDate, endDate);

        // Validate date range
        if (startDate.isAfter(endDate)) {
            logger.warn("Invalid date range: startDate {} is after endDate {}", startDate, endDate);
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        // Retrieve attendance records within the date range
        List<Attendance> records = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        if (records.isEmpty()) {
            logger.warn("No attendance records found for employeeId: {} between {} and {}", employeeId, startDate, endDate);
            return 0.0; // Return 0 if no records are found
        }

        // Calculate total hours
        double totalHours = 0.0;
        for (Attendance record : records) {
            try {
                LocalTime checkIn = record.getCheckInTime();
                LocalTime checkOut = record.getCheckOutTime();

                if (checkIn == null || checkOut == null) {
                    logger.warn("Skipping recordId {}: Check-in or check-out time is missing", record.getId());
                    continue; // Skip records with missing check-in or check-out times
                }

                Duration duration = Duration.between(checkIn, checkOut);
                double hours = duration.toMinutes() / 60.0;
                totalHours += hours;
                logger.debug("RecordId {}: {} hours", record.getId(), hours);
            } catch (Exception e) {
                logger.error("Error calculating hours for recordId {}: {}", record.getId(), e.getMessage());
                // Continue with the next record instead of failing the entire operation
            }
        }

        logger.info("Total working hours for employeeId {}: {}", employeeId, totalHours);
        return totalHours;
    }

    public Leave saveLeaveRequest(Long employeeId, LocalDate startDate, LocalDate endDate, String reason, String leaveType) {
        logger.info("Saving leave request for employeeId: {} from {} to {} with leaveType: {}",
                employeeId, startDate, endDate, leaveType);

        // Validate that endDate is not before startDate
        if (endDate.isBefore(startDate)) {
            logger.warn("Invalid date range: endDate {} is before startDate {}", endDate, startDate);
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Validate leave type
        String normalizedLeaveType = leaveType.trim().toUpperCase();
        if (!normalizedLeaveType.equals("ANNUAL") && !normalizedLeaveType.equals("CASUAL") && !normalizedLeaveType.equals("HALF DAY")) {
            logger.warn("Invalid leave type: {}", leaveType);
            throw new IllegalArgumentException("Leave type must be 'Annual', 'Casual', or 'Half Day'");
        }

        // Calculate number of leave days
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Check monthly limits
        LocalDate monthStart = startDate.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<Leave> existingLeaves = leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, monthStart, monthEnd);

        long annualDays = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("ANNUAL"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long casualDays = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("CASUAL"))
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        long halfDayCount = existingLeaves.stream()
                .filter(l -> l.getLeaveType().equalsIgnoreCase("HALF DAY"))
                .count();

        if (normalizedLeaveType.equals("ANNUAL") && annualDays + daysBetween > 14) {
            logger.warn("Annual leave limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 14 annual leave days allowed per month");
        }
        if (normalizedLeaveType.equals("CASUAL") && casualDays + daysBetween > 7) {
            logger.warn("Casual leave limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 7 casual leave days allowed per month");
        }
        if (normalizedLeaveType.equals("HALF DAY") && halfDayCount + 1 > 2) {
            logger.warn("Half day limit exceeded for employeeId: {}", employeeId);
            throw new IllegalArgumentException("Maximum 2 half days allowed per month");
        }

        // Create and save the leave request
        Leave leave = new Leave(employeeId, startDate, endDate, reason, "PENDING", normalizedLeaveType);
        Leave savedLeave = leaveRepository.save(leave);
        logger.info("Leave request saved successfully for employeeId: {}", employeeId);
        return savedLeave;
    }

    // New method to get attendance count per month
    public long getAttendanceCountPerMonth(Long employeeId, LocalDate month) {
        logger.info("Calculating attendance count for employeeId: {} for month: {}", employeeId, month);

        // Determine the start and end of the month
        LocalDate monthStart = month.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        // Retrieve attendance records within the month
        List<Attendance> records = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, monthStart, monthEnd);

        // Count distinct days where both check-in and check-out times are present
        long attendedDays = records.stream()
                .filter(record -> record.getCheckInTime() != null && record.getCheckOutTime() != null)
                .map(Attendance::getDate)
                .distinct()
                .count();

        logger.info("Attendance count for employeeId {} in month {}: {}", employeeId, month, attendedDays);
        return attendedDays;
    }
}