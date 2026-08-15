package org.example.crm.entity.dto.group;

import org.example.crm.entity.dto.teacher.TeacherDto;
import org.example.crm.entity.dto.timeTable.TimeTableDto;
import org.example.crm.entity.enums.GroupLevel;
import org.example.crm.entity.enums.GroupStatus;

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
