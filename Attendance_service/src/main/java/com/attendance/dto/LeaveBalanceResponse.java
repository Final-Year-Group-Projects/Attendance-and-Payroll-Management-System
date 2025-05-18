package com.attendance.dto;

public class LeaveBalanceResponse {

    private long remainingAnnualDays;
    private long remainingCasualDays;
    private long remainingHalfDays;

    public LeaveBalanceResponse(long remainingAnnualDays, long remainingCasualDays, long remainingHalfDays) {
        this.remainingAnnualDays = remainingAnnualDays;
        this.remainingCasualDays = remainingCasualDays;
        this.remainingHalfDays = remainingHalfDays;
    }

    public long getRemainingAnnualDays() {
        return remainingAnnualDays;
    }

    public void setRemainingAnnualDays(long remainingAnnualDays) {
        this.remainingAnnualDays = remainingAnnualDays;
    }

    public long getRemainingCasualDays() {
        return remainingCasualDays;
    }

    public void setRemainingCasualDays(long remainingCasualDays) {
        this.remainingCasualDays = remainingCasualDays;
    }

    public long getRemainingHalfDays() {
        return remainingHalfDays;
    }

    public void setRemainingHalfDays(long remainingHalfDays) {
        this.remainingHalfDays = remainingHalfDays;
    }
}