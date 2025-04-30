package com.attendance.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LeaveRequest {

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    // Getters and setters
    @SuppressWarnings("unused") // Suppress unused warning since used by Spring Boot
    public LocalDate getStartDate() {
        return startDate;
    }

    @SuppressWarnings("unused")
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @SuppressWarnings("unused")
    public LocalDate getEndDate() {
        return endDate;
    }

    @SuppressWarnings("unused")
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @SuppressWarnings("unused")
    public String getReason() {
        return reason;
    }

    @SuppressWarnings("unused")
    public void setReason(String reason) {
        this.reason = reason;
    }
}