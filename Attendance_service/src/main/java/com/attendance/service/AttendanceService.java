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

    public Leave saveLeaveRequest(Long employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        logger.info("Saving leave request for employeeId: {} from {} to {}", employeeId, startDate, endDate);
        // Validate that endDate is not before startDate
        if (endDate.isBefore(startDate)) {
            logger.warn("Invalid date range: endDate {} is before startDate {}", endDate, startDate);
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Create a new Leave entity with status PENDING
        Leave leave = new Leave(employeeId, startDate, endDate, reason, "PENDING");
        Leave savedLeave = leaveRepository.save(leave);
        logger.info("Leave request saved successfully for employeeId: {}", employeeId);
        return savedLeave;
    }
}