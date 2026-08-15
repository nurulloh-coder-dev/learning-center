package org.example.learningcenter.projection;

import org.example.learningcenter.entity.enums.DayType;
import org.example.learningcenter.entity.enums.GroupStatus;
import org.example.learningcenter.entity.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface EnrollmentProjection {
    String getId();
    String getStudentId();
    String getStudentUserId();
    String getStudentImageUrl();
    String getStudentFullName();
    String getStudentPhone();
    LocalDate getStudentBirthDate();
    Role getStudentRole();
    String getParentPhone();
    String getGroupId();
    String getGroupName();
    String getRoom();
    String getTeacherId();
    String getTeacherUserId();
    String getTeacherImageUrl();
    String getTeacherFullName();
    String getTeacherPhone();
    LocalDate getTeacherBirthDate();
    Role getTeacherRole();
    String getTimeTableId();
    DayType getTimeTableDayType();
    LocalTime getTimeTableStartTime();
    LocalTime getTimeTableEndTime();
    GroupStatus getGroupStatus();
    LocalDateTime getEnrollmentDate();
}
