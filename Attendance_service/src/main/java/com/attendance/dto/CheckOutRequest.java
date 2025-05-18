package com.attendance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CheckOutRequest {

    @NotNull(message = "Check-out time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}", message = "Check-out time must be in HH:mm:ss format")
    private String checkOutTime;

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}