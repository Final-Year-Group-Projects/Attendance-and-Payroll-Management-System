package com.attendance.controller;

import java.io.Serializable;

public class AttendanceRequest implements Serializable {
    private String date;
    private String checkInTime;
    private String checkOutTime;

    // Add getters/setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}