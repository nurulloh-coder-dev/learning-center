package org.example.crm.entity.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentCreateDto;

import java.util.List;

public record AttendanceCreateDto(
        @NotBlank String lessonId,
        @NotEmpty List<AttendanceStudentCreateDto> students
) {}