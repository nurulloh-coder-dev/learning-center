package org.example.crm.entity.dto;

import org.example.crm.entity.enums.DayType;

import java.time.LocalTime;

public record TimeTableCreateDto(
        DayType dayType,
        LocalTime startTime,
        LocalTime endTime
) {
}
