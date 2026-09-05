package org.example.crm.entity.dto.groupLevel;

import java.math.BigDecimal;

public record GroupLevelDto(
        String id,
        String name,
        Integer lessonCount,
        Integer orderNumber,
        Integer durationInMonths,
        BigDecimal monthlyFee
) {
}
