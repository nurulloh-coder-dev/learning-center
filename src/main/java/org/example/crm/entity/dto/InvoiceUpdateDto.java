package org.example.crm.entity.dto;

import org.example.crm.entity.enums.InvoiceStatus;

public record InvoiceUpdateDto(
        InvoiceStatus status
) {
}
