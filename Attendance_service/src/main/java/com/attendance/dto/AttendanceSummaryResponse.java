package com.attendance.dto;

public class AttendanceSummaryResponse {
    private String employeeId;
    private int month;
    private int year;
    private int workingDays;
    private int approvedLeaves;
    private int notApprovedLeaves;

    public AttendanceSummaryResponse() {}

    public AttendanceSummaryResponse(String employeeId, int month, int year, int workingDays, int approvedLeaves, int notApprovedLeaves) {
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.workingDays = workingDays;
        this.approvedLeaves = approvedLeaves;
        this.notApprovedLeaves = notApprovedLeaves;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public int getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(int approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public int getNotApprovedLeaves() {
        return notApprovedLeaves;
    }

    public void setNotApprovedLeaves(int notApprovedLeaves) {
        this.notApprovedLeaves = notApprovedLeaves;
    }
}