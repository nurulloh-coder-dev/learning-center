package org.example.learningcenter.projection;

import org.example.learningcenter.entity.enums.DayType;
import org.springframework.beans.factory.annotation.Value;

public interface GroupNameProjection {
    String getId();
    String getName();
    @Value("#{target.timeTable.dayType}")
    DayType getDayType();
}
