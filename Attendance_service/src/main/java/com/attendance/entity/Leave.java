package com.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "leave_type", nullable = false)
    private String leaveType; // New field to store "Annual", "Casual", or "Half Day"

    // Constructors
    @SuppressWarnings("unused")
    public Leave() {
        // Default constructor required by JPA
    }

    public Leave(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String status, String leaveType) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status != null ? status : "PENDING"; // Default to "PENDING" if not provided
        this.leaveType = leaveType;
    }

    // Getters and setters
    @SuppressWarnings("unused") // Suppress unused warning since these are used by JPA
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getEmployeeId() { // Changed from Long to String
        return employeeId;
    }

    @SuppressWarnings("unused")
    public void setEmployeeId(String employeeId) { // Changed from Long to String
        this.employeeId = employeeId;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public String getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(String status) {
        this.status = status != null ? status : "PENDING"; // Default to "PENDING" if not provided
    }

    public String getLeaveType() {
        return leaveType;
    }

    @SuppressWarnings("unused")
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}