package org.example.crm.filters;

import org.example.crm.entity.enums.GroupStatus;

public record GroupFilterDto(String search, GroupStatus status, String level) {
}
