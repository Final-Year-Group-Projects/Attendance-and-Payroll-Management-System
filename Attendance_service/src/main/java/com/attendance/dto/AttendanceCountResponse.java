package com.attendance.dto;

public class AttendanceCountResponse {

    private long attendedDays;

    public AttendanceCountResponse(long attendedDays) {
        this.attendedDays = attendedDays;
    }

    public long getAttendedDays() {
        return attendedDays;
    }

    public void setAttendedDays(long attendedDays) {
        this.attendedDays = attendedDays;
    }
}