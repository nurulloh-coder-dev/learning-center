package org.example.crm.projection;

import org.example.crm.entity.enums.DayType;
import org.springframework.beans.factory.annotation.Value;

public interface GroupNameProjection {
    String getId();
    String getName();
    DayType getDayType();
}
