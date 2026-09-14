package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreTenantAuditFields;
import org.example.crm.entity.dto.teacher.TeacherCreateDto;
import org.example.crm.entity.dto.teacher.TeacherDto;
import org.example.crm.entity.dto.teacher.TeacherUpdateDto;
import org.example.crm.entity.model.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TeacherMapper {
    @Mapping(source = "user",target = "userDto")
    TeacherDto toDto(Teacher teacher);

    @IgnoreTenantAuditFields
    Teacher toEntity(TeacherCreateDto createDto);

    @IgnoreTenantAuditFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdate(@MappingTarget Teacher teacher, TeacherUpdateDto teacherUpdateDto);
}
