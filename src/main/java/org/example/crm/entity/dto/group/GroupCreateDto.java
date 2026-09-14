package org.example.crm.entity.dto.group;

import jakarta.annotation.Nonnull;
import org.example.crm.entity.dto.TimeTableCreateDto;

import java.time.LocalDate;

public record GroupCreateDto(
        @Nonnull
        String name,
        @Nonnull
        String room,
        LocalDate startDate,
        String teacherId,
        TimeTableCreateDto timeTable
) {

}
