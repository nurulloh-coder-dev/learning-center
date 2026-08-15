package org.example.crm.entity.dto;

import org.example.crm.entity.enums.DayType;

import java.time.LocalTime;

public record TimeTableUpdateDto(
        DayType dayType,
        LocalTime startTime,
        LocalTime endTime)
{}