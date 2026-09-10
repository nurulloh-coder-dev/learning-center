package org.example.crm.entity.dto.user;

public record UserCreatedResponseDto(
        String id,
        String fullName,
        String phone,
        String temporaryPassword
) {}