package org.example.crm.entity.dto.attendance;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentUpdateDto;

import java.util.List;

public record AttendanceUpdateDto(
        @NotNull List<AttendanceStudentUpdateDto> attendanceStudents
        ) {
}
