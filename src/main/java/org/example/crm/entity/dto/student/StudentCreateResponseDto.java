package org.example.crm.entity.dto.student;

import org.example.crm.entity.dto.user.UserCreatedResponseDto;

import java.math.BigDecimal;

public record StudentCreateResponseDto(
        String id,
        UserCreatedResponseDto userDto,
        String parentPhone,
        BigDecimal balance
) {
}
