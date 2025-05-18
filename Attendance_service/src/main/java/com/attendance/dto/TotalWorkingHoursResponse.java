package com.attendance.dto;

public class TotalWorkingHoursResponse {

    private double totalHours;

    public TotalWorkingHoursResponse(double totalHours) {
        this.totalHours = totalHours;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }
}