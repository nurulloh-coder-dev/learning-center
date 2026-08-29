package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.student.StudentUpdateDto;
import org.example.crm.entity.model.Student;
import org.example.crm.projection.StudentProjection;
import org.example.crm.projection.StudentShowProjection;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parentPhone", target = "parentPhone")
    @Mapping(source = "user.fullName", target = "userDto.fullName")
    @Mapping(source = "user.phone", target = "userDto.phone")
    @Mapping(source = "user.birthDate", target = "userDto.birthDate")
    @Mapping(source = "user.id", target = "userDto.id")
    StudentDto toDtoProj(StudentProjection projection);

    @Mapping(source = "user", target = "userDto")
    StudentDto toDto(Student student);


    @Mapping(source = "userCreateDto", target = "user")
    Student toEntity(StudentCreateDto studentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdate(@MappingTarget Student student, StudentUpdateDto updateDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parentPhone", target = "parentPhone")
    @Mapping(source = "fullName", target = "userDto.fullName")
    @Mapping(source = "phone", target = "userDto.phone")
    @Mapping(source = "imageUrl", target = "userDto.imageUrl")
    @Mapping(source = "birthDate", target = "userDto.birthDate")
    StudentDto toDtoShowProj(StudentShowProjection studentShowProjection);
}