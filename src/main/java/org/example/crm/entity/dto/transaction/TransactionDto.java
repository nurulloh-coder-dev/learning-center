package org.example.crm.entity.dto.transaction;

import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        String id,
        TransactionType type,
        BigDecimal amount,
        String note,
        InvoiceDto invoice,
        StudentDto user,
        LocalDateTime createdAt
) {
}