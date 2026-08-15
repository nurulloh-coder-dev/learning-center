package org.example.crm.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.crm.entity.dto.TimeTableCreateDto;

public record GroupCreateDto(
        @Nonnull
        String name,
        @Nonnull
        String room,
        String teacherId,
        TimeTableCreateDto timeTable
) {

}
