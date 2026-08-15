package org.example.learningcenter.entity.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.example.learningcenter.entity.dto.attendanceStudent.AttendanceStudentCreateDto;
import org.example.learningcenter.entity.dto.attendanceStudent.AttendanceStudentDto;

import java.util.List;

public record AttendanceCreateDto(
        @NotBlank String lessonId,
        @NotEmpty List<AttendanceStudentCreateDto> students
) {}