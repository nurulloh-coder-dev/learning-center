package org.example.crm.entity.dto;

import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.InvoiceType;
import org.example.crm.entity.model.Enrollment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceDto(
        String id,
        String invoiceNumber,
        BigDecimal amount,
        LocalDateTime issuedAt,
        InvoiceType type,
        EnrollmentDto enrollmentDto
) {
}
