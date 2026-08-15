package org.example.crm.entity.dto.attendanceStudent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.AttendanceStatus;

public record AttendanceStudentCreateDto(
        @NotBlank String studentId,
        @NotNull AttendanceStatus status
        ) {
}
