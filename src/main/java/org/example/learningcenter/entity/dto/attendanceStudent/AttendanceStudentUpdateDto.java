package org.example.learningcenter.entity.dto.attendanceStudent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.learningcenter.entity.enums.AttendanceStatus;

public record AttendanceStudentUpdateDto(
        @NotBlank String studentId,
        @NotNull AttendanceStatus status
) {
}
