package com.attendance.dto;

public class WorkingHoursResponse {

    private double hours;

    public WorkingHoursResponse(double hours) {
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}