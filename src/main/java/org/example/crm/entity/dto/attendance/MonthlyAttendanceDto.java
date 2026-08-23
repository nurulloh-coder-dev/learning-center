package org.example.crm.entity.dto.attendance;

import org.example.crm.entity.enums.AttendanceStatus;

import java.time.LocalDate;
import java.util.Map;

public record MonthlyAttendanceDto(String id, String lessonTitle, LocalDate date, Map<String, AttendanceStatus> attendanceStudentMap) {

}
