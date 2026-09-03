package org.example.crm.entity.dto.groupLevel;

import java.math.BigDecimal;

public record GroupLevelUpdateDto(
        Integer lessonCount,
        Integer durationInMonths,
        BigDecimal monthlyFee
) {
}
