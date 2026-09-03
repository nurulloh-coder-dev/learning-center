package org.example.crm.entity.dto.groupLevel;

import java.math.BigDecimal;

public record GroupLevelCreateDto(
        String name,
        Integer orderNumber,
        Integer lessonCount,
        Integer durationInMonths,
        BigDecimal monthlyFee
) {
}
