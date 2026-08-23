package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentCreateDto;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentDto;
import org.example.crm.entity.model.AttendanceStudent;
import org.example.crm.projection.AttendanceStudentProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = StudentMapper.class)
public interface AttendanceStudentMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.user.fullName", target = "studentFullName")
    AttendanceStudentDto toDto(AttendanceStudent attendanceStudent);

    @IgnoreAuditFields
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(target = "attendance", ignore = true)
    AttendanceStudent toEntity(AttendanceStudentCreateDto createDto);

    List<AttendanceStudentDto> toDto(List<AttendanceStudentProjection> attendanceStudentsByAttId);
}
