package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.course.CourseCreateDto;
import org.example.crm.entity.dto.course.CourseDto;
import org.example.crm.entity.dto.course.CourseUpdateDto;
import org.example.crm.entity.model.Course;
import org.example.crm.projection.CourseProjection;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto toDto(Course course);

    CourseDto toDtoFromProjection(CourseProjection projection);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "organizationId", ignore = true) // Handled by BaseEntity @PrePersist
    Course toEntity(CourseCreateDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    void mapUpdate(@MappingTarget Course course, CourseUpdateDto updateDto);
}