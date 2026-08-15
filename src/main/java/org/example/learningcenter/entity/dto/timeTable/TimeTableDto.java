package org.example.learningcenter.entity.dto.timeTable;

import org.example.learningcenter.entity.enums.DayType;

import java.time.LocalTime;

public record TimeTableDto(
        String id,
        DayType dayType,
        LocalTime startTime,
        LocalTime endTime
) {
}
