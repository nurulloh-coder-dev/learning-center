package org.example.crm.projection;

import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.TimeTable;

import java.time.LocalDate;

public interface GroupProjection {
    String getId();
    String getName();
    String getRoom();
    LocalDate getStartDate();
    String getTeacherId();
    String getTeacherFullName();
    TimeTable getTimeTable();
    GroupStatus getStatus();
    String getLevelName();
    Integer getCurrentMonth();
    Integer getLessonsCount();
    Integer getStudentCount();
}
