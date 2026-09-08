package org.example.crm.entity.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionCreateDto(
        @NotNull
        @Schema(allowableValues = {"PAID", "RETURNED"})
        TransactionType type,

        @NotNull
        @DecimalMin(value = "0.0", message = "amount cannot be negative")
        BigDecimal amount,

        @NotNull
        String studentId
) {
}