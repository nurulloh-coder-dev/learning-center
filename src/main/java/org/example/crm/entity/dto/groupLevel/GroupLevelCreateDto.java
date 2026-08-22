package org.example.crm.entity.dto.groupLevel;

public record GroupLevelCreateDto(
        String name,
        Integer orderNumber,
        Integer lessonCount,
        Integer durationInMonths
) {
}
