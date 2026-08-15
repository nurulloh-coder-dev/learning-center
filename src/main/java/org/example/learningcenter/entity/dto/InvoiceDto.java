package org.example.learningcenter.entity.dto;

import org.example.learningcenter.entity.dto.group.GroupDto;
import org.example.learningcenter.entity.dto.student.StudentDto;
import org.example.learningcenter.entity.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceDto(
        String id,
        String invoiceNumber,
        StudentDto student,
        BigDecimal amount,
        LocalDateTime issuedAt,
        InvoiceStatus status
) {
}
