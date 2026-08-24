package org.example.crm.projection;

import org.example.crm.entity.enums.AttendanceStatus;

public interface AttendanceStudentProjection {
    String getAttendanceId();
    String getStudentId();
    AttendanceStatus getStatus();
}