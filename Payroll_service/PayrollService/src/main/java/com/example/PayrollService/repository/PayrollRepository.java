package com.example.PayrollService.repository;

import com.example.PayrollService.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PayrollRepository extends JpaRepository<PayrollRecord, Long> {
    List<PayrollRecord> findByEmployeeId(Long employeeId);

    @Query("SELECT DISTINCT p.employeeId FROM PayrollRecord p")
    List<Long> findAllEmployeeIdsDistinct();

    Optional<PayrollRecord> findById(Long id);

}
