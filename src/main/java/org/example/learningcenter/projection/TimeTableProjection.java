package org.example.learningcenter.projection;

import org.example.learningcenter.entity.enums.DayType;

import java.time.LocalTime;

public interface TimeTableProjection {
    String getId();
    DayType getDayType();
    LocalTime getStartTime();
    LocalTime getEndTime();
}
