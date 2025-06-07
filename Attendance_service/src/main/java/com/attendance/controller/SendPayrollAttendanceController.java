package com.attendance.controller;

import com.attendance.dto.AttendanceDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class SendPayrollAttendanceController {

    @GetMapping("/{employeeId}/details")
    public AttendanceDTO getAttendanceDetails(
            @PathVariable String employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return new AttendanceDTO(employeeId, month, year, 22, 2, 1);
    }
}
