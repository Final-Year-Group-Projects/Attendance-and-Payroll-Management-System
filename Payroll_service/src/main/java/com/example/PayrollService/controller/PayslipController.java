package com.example.PayrollService.controller;

import com.example.PayrollService.entity.PayrollRecord;
import com.example.PayrollService.exception.ResourceNotFoundException;
import com.example.PayrollService.repository.PayrollRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@RestController
@RequestMapping("/payrolls")
public class PayslipController {

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping(value = "/{payrollId}/payslip", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getPayslip(
            @PathVariable @Min(1) Long payrollId,
            HttpServletRequest request) {

        // Extract the userId from request set by the TokenValidationFilter
        String tokenUserId = (String) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        // Fetch the payroll record
        PayrollRecord record = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found for ID " + payrollId));

        // Check if the token's userId matches the employeeId in the payroll record
        if ("employee".equalsIgnoreCase(role)) {
            if (!record.getEmployeeId().toString().equals(tokenUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("<h2>Access denied: This payslip does not belong to you.</h2>");
            }
        }

        String html = "<html><body style='font-family:sans-serif;'>"
                + "<h2>Payslip for Employee ID: " + record.getEmployeeId() + "</h2>"
                + "<table border='1' cellpadding='10'>"
                + "<tr><td><b>Basic Salary</b></td><td>" + record.getBasicSalary() + "</td></tr>"
                + "<tr><td><b>Medical Allowance</b></td><td>" + record.getMedicalAllowance() + "</td></tr>"
                + "<tr><td><b>Medical Allowance</b></td><td>" + record.getOtherAllowance() + "</td></tr>"
                + "<tr><td><b>Tax Deduction</b></td><td>" + record.getTaxDeduction() + "</td></tr>"
                + "<tr><td><b>Transport Fee</b></td><td>" + record.getTransportFee() + "</td></tr>"
                + "<tr><td><b>Sports Fee</b></td><td>" + record.getSportsFee() + "</td></tr>"
                + "<tr><td><b>Working Days</b></td><td>" + record.getWorkingDays() + "</td></tr>"
                + "<tr><td><b>Approved Leaves</b></td><td>" + record.getApprovedLeaves() + "</td></tr>"
                + "<tr><td><b>Unapproved Leaves</b></td><td>" + record.getNotApprovedLeaves() + "</td></tr>"
                + "<tr><td><b>No Pay</b></td><td>" + record.getNoPay() + "</td></tr>"
                + "<tr><td><b>Net Salary</b></td><td><b>" + record.getNetSalary() + "</b></td></tr>"
                + "<tr><td><b>Generated Date</b></td><td>" + record.getGeneratedDate() + "</td></tr>"
                + "</table><br>"
                + "<a href='/payrolls/" + payrollId + "/payslip/pdf' download>"
                + "<button style='margin-top:20px;padding:10px;font-size:14px;'>Download Payslip as PDF</button>"
                + "</a>"
                + "<br><br><i>Note: This is a system-generated payslip.</i>"
                + "</body></html>";

        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/{payrollId}/payslip/pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPayslipPdf(@PathVariable @Min(1) Long payrollId,
                                                     HttpServletRequest request) {

        String tokenUserId = (String) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        Optional<PayrollRecord> optional = Optional.ofNullable(payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found for ID " + payrollId)));

        if (optional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        PayrollRecord record = optional.get();

        if ("employee".equalsIgnoreCase(role)) {
            if (!record.getEmployeeId().toString().equals(tokenUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.TEXT_HTML)
                        .body("Access denied: You are not authorized to access payslip for other employees.".getBytes());
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font contentFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("Employee Payslip", titleFont));
            document.add(new Paragraph("Generated Date: " + record.getGeneratedDate(), contentFont));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            table.addCell("Employee ID");
            table.addCell(record.getEmployeeId().toString());
            table.addCell("Basic Salary");
            table.addCell(record.getBasicSalary().toString());
            table.addCell("Working Days");
            table.addCell(record.getWorkingDays().toString());
            table.addCell("Approved Leaves");
            table.addCell(record.getApprovedLeaves().toString());
            table.addCell("Unapproved Leaves");
            table.addCell(record.getNotApprovedLeaves().toString());

            // Allowances
            table.addCell("Medical Allowance");
            table.addCell(record.getMedicalAllowance().toString());
            table.addCell("Transport Fee");
            table.addCell(record.getTransportFee().toString());

            // Deductions
            table.addCell("Sports Fee");
            table.addCell(record.getSportsFee().toString());
            table.addCell("Tax Deduction");
            table.addCell(record.getTaxDeduction().toString());
            table.addCell("No Pay");
            table.addCell(record.getNoPay().toString());

            // Net Salary
            table.addCell("Net Salary");
            table.addCell(record.getNetSalary().toString());

            document.add(table);
            document.close();

            byte[] pdfBytes = out.toByteArray();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=payslip_" + payrollId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
