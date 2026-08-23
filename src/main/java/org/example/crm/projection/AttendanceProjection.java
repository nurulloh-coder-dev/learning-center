package org.example.crm.projection;

import java.time.LocalDateTime;

public interface AttendanceProjection {
    String getId();
    LocalDateTime getDate();
    String getLessonTitle();
}
