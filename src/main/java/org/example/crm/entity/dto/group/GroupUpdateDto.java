package org.example.crm.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.crm.entity.dto.TimeTableUpdateDto;
import org.example.crm.entity.enums.GroupStatus;

import java.time.LocalDate;

public record GroupUpdateDto(
        @Nonnull
        String name,
        @Nonnull
        String room,
        @Nonnull
        LocalDate startDate,
        String teacherId,
        TimeTableUpdateDto timeTable
) {
}
