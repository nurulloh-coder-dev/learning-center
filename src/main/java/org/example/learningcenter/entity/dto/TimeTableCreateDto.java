package org.example.learningcenter.entity.dto;

import org.example.learningcenter.entity.enums.DayType;

import java.time.LocalTime;

public record TimeTableCreateDto(
        DayType dayType,
        LocalTime startTime,
        LocalTime endTime
) {
}
