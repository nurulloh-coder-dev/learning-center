package org.example.crm.entity.dto.groupLevel;

public record GroupLevelDto(
        String id,
        String name,
        Integer lessonCount,
        Integer orderNumber,
        Integer durationInMonths
) {
}
