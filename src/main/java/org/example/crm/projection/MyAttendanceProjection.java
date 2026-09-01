package org.example.crm.projection;

import org.example.crm.entity.enums.AttendanceStatus;

import java.time.LocalDateTime;

public interface MyAttendanceProjection {
    String getTitle();
    LocalDateTime getDate();
    AttendanceStatus getStatus();
    String getReason();
}
