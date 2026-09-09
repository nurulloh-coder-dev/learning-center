package org.example.crm.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InvoiceCreateDto(
        @NotNull String  enrollmentId,
        @NotNull BigDecimal amount,
        @NotNull String levelId,
        @Min(value = 1)
        @NotNull Integer month
        ) {
}
