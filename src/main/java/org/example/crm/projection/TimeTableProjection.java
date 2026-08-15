package org.example.crm.projection;

import org.example.crm.entity.enums.DayType;

import java.time.LocalTime;

public interface TimeTableProjection {
    String getId();
    DayType getDayType();
    LocalTime getStartTime();
    LocalTime getEndTime();
}
