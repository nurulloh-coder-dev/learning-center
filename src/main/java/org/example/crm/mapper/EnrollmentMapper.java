package org.example.crm.mapper;

import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "student.id",target = "studentId")
    @Mapping(source = "group.id",target = "groupId")
    @Mapping(source = "monthlyFee",target = "monthlyFee")
    @Mapping(source = "paidAmount",target = "paidAmount")
    @Mapping(source = "status",target = "status")
    EnrollmentDto toDto(Enrollment enrollment);
}
