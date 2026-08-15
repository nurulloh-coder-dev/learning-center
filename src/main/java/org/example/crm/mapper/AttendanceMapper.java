package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.attendance.AttendanceCreateDto;
import org.example.crm.entity.dto.attendance.AttendanceDto;
import org.example.crm.entity.model.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LessonMapper.class, AttendanceStudentMapper.class})
public interface AttendanceMapper {

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "createdAt", target = "createdAt")
    AttendanceDto toDto(Attendance attendance);

    @IgnoreAuditFields
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "attendanceStudents", ignore = true) // Managed in service
    Attendance toEntity(AttendanceCreateDto createDto);
}