package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.annotation.IgnoreAuditFields;
import org.example.learningcenter.entity.dto.lesson.LessonCreateDto;
import org.example.learningcenter.entity.dto.lesson.LessonDto;
import org.example.learningcenter.entity.dto.lesson.LessonUpdateDto;
import org.example.learningcenter.entity.model.Lesson;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

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
