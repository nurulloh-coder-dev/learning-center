package org.example.crm.filters;

import org.example.crm.entity.enums.LeadStatus;

public record LeadFilterDto(String search, LeadStatus status) {
}
