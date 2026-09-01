package org.example.crm.entity.dto.attendance;

import org.example.crm.entity.enums.AttendanceStatus;

import java.time.LocalDate;

public record MyAttendanceDto(String title, LocalDate date, AttendanceStatus status, String reason) {
}
