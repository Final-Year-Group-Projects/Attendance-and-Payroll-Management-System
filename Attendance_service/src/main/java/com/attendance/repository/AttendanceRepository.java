package com.attendance.repository;

import com.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Must extend JpaRepository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeId(Long employeeId);
}