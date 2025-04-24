package com.attendance.controller;

import com.attendance.entity.Attendance;
import com.attendance.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public static class AttendanceRequest {
        private String date;
        private String checkInTime;
        private String checkOutTime;

        // Getters and setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getCheckInTime() { return checkInTime; }
        public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
        public String getCheckOutTime() { return checkOutTime; }
        public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<Attendance> recordAttendance(
            @PathVariable Long employeeId,
            @RequestBody AttendanceRequest request) {
        logger.info("Recording attendance for employeeId: {}", employeeId);

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(LocalDate.parse(request.getDate()));
        attendance.setCheckInTime(LocalTime.parse(request.getCheckInTime()));
        attendance.setCheckOutTime(LocalTime.parse(request.getCheckOutTime()));

        Attendance savedAttendance = attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok(savedAttendance);
    }
}