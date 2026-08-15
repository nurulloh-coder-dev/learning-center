package org.example.learningcenter.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.learningcenter.entity.dto.TimeTableUpdateDto;
import org.example.learningcenter.entity.enums.GroupStatus;

public record GroupUpdateDto(
        @Nonnull
        String name,
        @Nonnull
        String room,
        String teacherId,
        TimeTableUpdateDto timeTable,
        GroupStatus status
) {
}
