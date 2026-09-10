package org.example.crm.filters;

import org.example.crm.entity.enums.InvoiceStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public record InvoiceFilterDto(LocalDateTime from, LocalDateTime to, InvoiceStatus status,
                               String search) {
}
