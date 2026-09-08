package org.example.crm.entity.dto;

import jakarta.validation.constraints.NotNull;

public record InvoiceCreateDto(
        @NotNull String enrollmentId
) {
}
