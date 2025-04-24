package com.attendance.service;

import com.attendance.entity.Attendance;
import com.attendance.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
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
}