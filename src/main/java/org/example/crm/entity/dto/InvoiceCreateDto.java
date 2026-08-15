package org.example.crm.entity.dto;

import java.math.BigDecimal;

public record InvoiceCreateDto(
        String studentId,
        BigDecimal amount
) {
}
