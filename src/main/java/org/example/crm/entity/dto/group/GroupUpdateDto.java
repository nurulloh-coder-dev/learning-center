package org.example.crm.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.crm.entity.dto.TimeTableUpdateDto;
import org.example.crm.entity.enums.GroupStatus;

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
