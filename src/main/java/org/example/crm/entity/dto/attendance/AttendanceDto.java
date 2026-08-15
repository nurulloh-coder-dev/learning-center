package org.example.crm.entity.dto.attendance;

import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentDto;

import java.time.LocalDateTime;
import java.util.List;

public record AttendanceDto(
        String id,
        String lessonId,
        List<AttendanceStudentDto> attendanceStudents,
        LocalDateTime createdAt
) {}