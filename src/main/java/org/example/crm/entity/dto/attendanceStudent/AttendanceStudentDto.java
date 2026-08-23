package org.example.crm.entity.dto.attendanceStudent;

import org.example.crm.entity.enums.AttendanceStatus;

public record AttendanceStudentDto(
        String studentId,
        String studentFullName,
        String studentImageUrl,
        AttendanceStatus status
) {}