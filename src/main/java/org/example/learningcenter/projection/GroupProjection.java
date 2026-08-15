package org.example.learningcenter.projection;

import org.example.learningcenter.entity.enums.GroupLevel;
import org.example.learningcenter.entity.enums.GroupStatus;
import org.example.learningcenter.entity.model.Teacher;
import org.example.learningcenter.entity.model.TimeTable;

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
