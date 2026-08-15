package org.example.learningcenter.entity.dto;

import java.math.BigDecimal;

public record InvoiceCreateDto(
        String studentId,
        BigDecimal amount
) {
}
