package org.example.crm.entity.dto.group;


import org.example.crm.entity.dto.teacher.TeacherIdNameDto;
import org.example.crm.entity.dto.timeTable.TimeTableDto;
import org.example.crm.entity.enums.GroupStatus;

import java.time.LocalDate;

public record GroupOverviewDto(
        String id,
        String name,
        String room,
        LocalDate startDate,
        GroupStatus status,
        String levelName,
        TeacherIdNameDto teacher,
        TimeTableDto timeTable,
        Integer currentMonth,
        Integer lessonsCount,
        Integer activeStudentsCount
) {}