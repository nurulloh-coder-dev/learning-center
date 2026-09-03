package org.example.crm.entity.dto.group;

import org.example.crm.entity.dto.groupLevel.GroupLevelDto;
import org.example.crm.entity.dto.teacher.TeacherDto;
import org.example.crm.entity.dto.timeTable.TimeTableDto;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.Level;

public record GroupDto(
        String id,
        String name,
        String room,
        TeacherDto teacher,
        TimeTableDto timeTable,
        GroupStatus status,
        GroupLevelDto level,
        Integer currentMonth,
        Integer lessonsCount
) {
}
