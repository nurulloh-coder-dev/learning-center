package org.example.crm.filters;

public record TimeTableFilterDto(org.example.crm.entity.enums.DayType dayType, java.time.LocalTime start,
                                 java.time.LocalTime end) {
}
