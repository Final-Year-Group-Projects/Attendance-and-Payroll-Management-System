package com.attendance.dto;

public class AttendanceDTO {
    private String employeeId;
    private Integer month;
    private Integer year;
    private Integer workingDays;
    private Integer approvedLeaves;
    private Integer notApprovedLeaves;

    // Constructor
    public AttendanceDTO(String employeeId, Integer month, Integer year,
                         Integer workingDays, Integer approvedLeaves, Integer notApprovedLeaves) {
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.workingDays = workingDays;
        this.approvedLeaves = approvedLeaves;
        this.notApprovedLeaves = notApprovedLeaves;
    }

    // Getters and setters (or use Lombok @Data to generate automatically)

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    public Integer getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(Integer approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public Integer getNotApprovedLeaves() {
        return notApprovedLeaves;
    }

    public void setNotApprovedLeaves(Integer notApprovedLeaves) {
        this.notApprovedLeaves = notApprovedLeaves;
    }
}
