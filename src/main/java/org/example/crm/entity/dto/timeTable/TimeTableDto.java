package org.example.crm.entity.dto.timeTable;

import org.example.crm.entity.enums.DayType;

import java.time.LocalTime;

public record TimeTableDto(
        String id,
        DayType dayType,
        LocalTime startTime,
        LocalTime endTime
) {
}
