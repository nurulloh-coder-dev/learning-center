package org.example.crm.entity.dto;

import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.InvoiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceDto(
        String id,
        String invoiceNumber,
        StudentDto student,
        BigDecimal amount,
        LocalDateTime issuedAt,
        InvoiceStatus status,
        InvoiceType type
) {
}
