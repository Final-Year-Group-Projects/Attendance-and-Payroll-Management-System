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
    private Long employeeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    // Constructors
    @SuppressWarnings("unused")
    public Leave() {
    }

    public Leave(Long employeeId, LocalDate startDate, LocalDate endDate, String reason, String status) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
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
    public Long getEmployeeId() {
        return employeeId;
    }

    @SuppressWarnings("unused")
    public void setEmployeeId(Long employeeId) {
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
        this.status = status;
    }
}