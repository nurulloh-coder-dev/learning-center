package org.example.learningcenter.entity.dto.attendanceStudent;

import org.example.learningcenter.entity.enums.AttendanceStatus;

public record AttendanceStudentDto(
        String studentId,
        String studentFullName,
        AttendanceStatus status
) {}