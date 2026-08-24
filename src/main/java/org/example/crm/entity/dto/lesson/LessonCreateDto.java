package org.example.crm.entity.dto.lesson;

import jakarta.annotation.Nonnull;

public record LessonCreateDto(
        @Nonnull String groupId,
        String topic) {
}
