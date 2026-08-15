package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.lesson.LessonCreateDto;
import org.example.crm.entity.dto.lesson.LessonDto;
import org.example.crm.entity.dto.lesson.LessonUpdateDto;
import org.example.crm.entity.model.Lesson;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class, GroupMapper.class})
public interface LessonMapper {

    @Mapping(source = "createdAt", target = "lessonDate")
    LessonDto toDto(Lesson lesson);

    @IgnoreAuditFields
    Lesson toEntity(LessonCreateDto createDto);

    @IgnoreAuditFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdate(@MappingTarget Lesson lesson, LessonUpdateDto updateDto);
}
