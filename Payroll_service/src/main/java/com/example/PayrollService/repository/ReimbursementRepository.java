// repository/ReimbursementRepository.java
package com.example.PayrollService.repository;

import com.example.PayrollService.entity.ReimbursementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReimbursementRepository extends JpaRepository<ReimbursementRecord, Long> {
    List<ReimbursementRecord> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);

}
