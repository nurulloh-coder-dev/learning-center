package org.example.learningcenter.entity.dto;

import org.example.learningcenter.entity.enums.InvoiceStatus;

public record InvoiceUpdateDto(
        InvoiceStatus status
) {
}
