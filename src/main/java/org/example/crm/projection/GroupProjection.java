package org.example.crm.projection;

import org.example.crm.entity.enums.GroupLevel;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.Teacher;
import org.example.crm.entity.model.TimeTable;

public interface GroupProjection {
    String getId();
    String getName();
    String getRoom();
    Teacher getTeacher();
    TimeTable getTimeTable();
    GroupStatus getStatus();
    GroupLevel getLevel();
    Integer getCurrentMonth();
    Integer getLessonsCount();
}
