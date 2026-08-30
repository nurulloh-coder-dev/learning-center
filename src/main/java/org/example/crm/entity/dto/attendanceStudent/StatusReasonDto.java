package org.example.crm.entity.dto.attendanceStudent;

import org.example.crm.entity.enums.AttendanceStatus;

public record StatusReasonDto(AttendanceStatus status, String reason) {
}
