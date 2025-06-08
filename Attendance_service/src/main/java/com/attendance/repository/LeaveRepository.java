package com.attendance.repository;

import com.attendance.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeIdAndStartDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);
    List<Leave> findByEmployeeId(String employeeId);
    List<Leave> findByEmployeeIdAndStartDateBetweenAndStatus(String employeeId, LocalDate startDate, LocalDate endDate, String status);
    List<Leave> findByEmployeeIdAndStartDateBetweenAndStatusNot(String employeeId, LocalDate startDate, LocalDate endDate, String status);
}