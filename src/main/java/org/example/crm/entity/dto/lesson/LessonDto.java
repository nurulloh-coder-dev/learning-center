package org.example.crm.entity.dto.lesson;

import org.example.crm.entity.dto.group.GroupDto;
import org.example.crm.entity.dto.teacher.TeacherDto;

import java.time.LocalDateTime;

public record LessonDto(
        String id,
        String lessonName,
        String lessonNumber,
        LocalDateTime lessonDate,
        Boolean isComplete,
        GroupDto group,
        TeacherDto teacherDto) {
}
