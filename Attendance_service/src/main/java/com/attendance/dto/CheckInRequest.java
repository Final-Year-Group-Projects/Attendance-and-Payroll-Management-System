package com.attendance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CheckInRequest {

    @NotNull(message = "Date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String date;

    @NotNull(message = "Check-in time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}", message = "Check-in time must be in HH:mm:ss format")
    private String checkInTime;

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
}