package com.attendance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AttendanceRequest {

    @NotNull(message = "Date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String date;

    @NotNull(message = "Check-in time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}", message = "Check-in time must be in HH:mm:ss format")
    private String checkInTime;

    @NotNull(message = "Check-out time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}", message = "Check-out time must be in HH:mm:ss format")
    private String checkOutTime;

    // Getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}