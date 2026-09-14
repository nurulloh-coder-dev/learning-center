package org.example.crm.entity.dto.organization;

import org.example.crm.entity.enums.Role;

public record OrganizationViewDto(String id, String name, Role role) {
}
