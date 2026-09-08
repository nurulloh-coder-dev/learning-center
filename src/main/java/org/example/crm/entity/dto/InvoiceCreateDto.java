package org.example.crm.entity.dto;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.model.Enrollment;

import java.math.BigDecimal;

public record InvoiceCreateDto(
        @NotNull Enrollment enrollment,
        @NotNull BigDecimal amount
        ) {
}
