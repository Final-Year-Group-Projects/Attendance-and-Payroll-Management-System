package com.attendance.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LeaveRequest {

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future (format: yyyy-MM-dd)")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the present or future (format: yyyy-MM-dd)")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotBlank(message = "Leave type is required")
    private String leaveType; // Must be "Annual", "Casual", or "Half Day"

    // Getters and setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}