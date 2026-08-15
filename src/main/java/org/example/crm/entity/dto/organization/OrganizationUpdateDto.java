package org.example.crm.entity.dto.organization;

import jakarta.annotation.Nonnull;

public record OrganizationUpdateDto(
        @Nonnull String name,
        String email,
        String phone,
        String website
) {
}
