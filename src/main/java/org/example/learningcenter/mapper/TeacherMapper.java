package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.annotation.IgnoreAuditFields;
import org.example.learningcenter.entity.dto.teacher.TeacherCreateDto;
import org.example.learningcenter.entity.dto.teacher.TeacherDto;
import org.example.learningcenter.entity.dto.teacher.TeacherUpdateDto;
import org.example.learningcenter.entity.model.Teacher;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TeacherMapper {
    @Mapping(source = "user",target = "userDto")
    TeacherDto toDto(Teacher teacher);


    @IgnoreAuditFields
    Teacher toEntity(TeacherCreateDto createDto);

    @IgnoreAuditFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdate(@MappingTarget Teacher teacher, TeacherUpdateDto teacherUpdateDto);
}
