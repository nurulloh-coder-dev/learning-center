package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.dto.enrollment.EnrollmentDto;
import org.example.learningcenter.entity.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "student.id",target = "studentId")
    @Mapping(source = "group.id",target = "groupId")
    EnrollmentDto toDto(Enrollment enrollment);
}
