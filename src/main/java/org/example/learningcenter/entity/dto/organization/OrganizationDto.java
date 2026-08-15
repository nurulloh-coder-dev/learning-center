package org.example.learningcenter.entity.dto.organization;

public record OrganizationDto(
        String id,
        String name,
        String email,
        String phone,
        String website
) {
}
