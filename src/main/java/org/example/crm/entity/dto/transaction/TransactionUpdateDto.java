package org.example.crm.entity.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionUpdateDto(
        TransactionType type,
        BigDecimal amount,
        String invoiceId,
        String studentId
) {
}