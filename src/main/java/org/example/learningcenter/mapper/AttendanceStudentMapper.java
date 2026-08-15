package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.annotation.IgnoreAuditFields;
import org.example.learningcenter.entity.dto.attendanceStudent.AttendanceStudentCreateDto;
import org.example.learningcenter.entity.dto.attendanceStudent.AttendanceStudentDto;
import org.example.learningcenter.entity.model.AttendanceStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = StudentMapper.class)
public interface AttendanceStudentMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.user.fullName", target = "studentFullName")
    AttendanceStudentDto toDto(AttendanceStudent attendanceStudent);

    @IgnoreAuditFields
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(target = "attendance", ignore = true)
    AttendanceStudent toEntity(AttendanceStudentCreateDto createDto);
}
