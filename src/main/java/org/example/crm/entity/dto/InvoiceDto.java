package org.example.crm.entity.dto;

import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceDto(
        String id,
        String invoiceNumber,
        BigDecimal amount,
        LocalDateTime issuedAt,
        EnrollmentDto enrollmentDto,
        InvoiceStatus paymentStatus
) {
}
