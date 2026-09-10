package org.example.crm.entity.dto.transaction;

import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionUpdateDto(
        TransactionType type,
        BigDecimal amount,
        String note,
        String invoiceId,
        String studentId
) {
}