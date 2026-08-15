package org.example.learningcenter.entity.dto.lesson;

import jakarta.annotation.Nonnull;

public record LessonCreateDto(
        @Nonnull String groupId,
        String lessonName) {
}
