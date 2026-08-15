package org.example.learningcenter.entity.dto.group;

import org.example.learningcenter.entity.dto.teacher.TeacherDto;
import org.example.learningcenter.entity.dto.timeTable.TimeTableDto;
import org.example.learningcenter.entity.enums.GroupLevel;
import org.example.learningcenter.entity.enums.GroupStatus;

public record GroupDto(
        String id,
        String name,
        String room,
        TeacherDto teacher,
        TimeTableDto timeTable,
        GroupStatus status,
        GroupLevel level,
        Integer currentMonth,
        Integer lessonsCount
) {
}
