package org.example.crm.filters;

import org.example.crm.entity.enums.TransactionType;

public record TransactionFilterDto(TransactionType type,String search) {
}
