package org.example.learningcenter.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.learningcenter.entity.dto.TimeTableCreateDto;
import org.example.learningcenter.entity.enums.GroupStatus;

public record GroupCreateDto(
        @Nonnull
        String name,
        @Nonnull
        String room,
        String teacherId,
        TimeTableCreateDto timeTable
) {

}
