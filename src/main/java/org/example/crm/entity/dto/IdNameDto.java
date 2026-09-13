package org.example.crm.entity.dto;

import lombok.Builder;

@Builder
public record IdNameDto(
        String id,
        String name
) {
}
