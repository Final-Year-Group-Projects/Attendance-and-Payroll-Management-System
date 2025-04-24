package com.attendance.repository;

import com.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

// Must extend JpaRepository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}