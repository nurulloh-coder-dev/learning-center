package org.example.crm.projection;

import org.example.crm.entity.enums.DayType;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.enums.Role;

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
