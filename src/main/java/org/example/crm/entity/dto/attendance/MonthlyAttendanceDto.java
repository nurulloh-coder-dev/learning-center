package org.example.crm.entity.dto.attendance;

import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentDto;

import java.time.LocalDate;
import java.util.List;

public record MonthlyAttendanceDto(String id, String lessonTitle, LocalDate date, List<AttendanceStudentDto> attendanceStudentDtos) {

}
