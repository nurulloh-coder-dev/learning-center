package org.example.crm.entity.dto.group;

public record GroupStatsDto(
        Long totalStudents,
        Long activeStudents,
        Long newStudents,
        Long lostStudents,
        Long potentialFailStudents, // students did not attend more than 3 classes in a row
        Long redList, // homework not done more than 2 times
        Long blackList
) {
}
