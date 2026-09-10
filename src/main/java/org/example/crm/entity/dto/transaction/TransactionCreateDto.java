package org.example.crm.entity.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionCreateDto(
        @NotNull
        @Schema(allowableValues = {"PAID", "RETURNED","MONTHLY_FEE","CORRECTION"})
        TransactionType type,

        @NotNull
        BigDecimal amount,

        String note,

        @NotNull
        String studentId,

        @NotNull
        String invoiceId
) {
}