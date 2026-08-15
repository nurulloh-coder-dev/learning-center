package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.TimeTableCreateDto;
import org.example.crm.entity.dto.TimeTableUpdateDto;
import org.example.crm.entity.dto.timeTable.TimeTableDto;
import org.example.crm.entity.model.TimeTable;
import org.example.crm.projection.TimeTableProjection;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeTableMapper {
    public TimeTableDto toDto(TimeTable timeTable) {
        return new TimeTableDto(
                timeTable.getId(),
                timeTable.getDayType(),
                timeTable.getStartTime(),
                timeTable.getEndTime()
        );
    }

    public TimeTableDto toDtoFromProjection(TimeTableProjection timeTableProjection) {
        return new TimeTableDto(
                timeTableProjection.getId(),
                timeTableProjection.getDayType(),
                timeTableProjection.getStartTime(),
                timeTableProjection.getEndTime()
        );
    }

    public TimeTable toEntity(TimeTableCreateDto createDto) {
        return new TimeTable(
                createDto.dayType(),
                createDto.startTime(),
                createDto.endTime()
        );
    }

    public void update(TimeTable timeTable, TimeTableUpdateDto updateDto) {
        if (updateDto.startTime() != null)
            timeTable.setStartTime(updateDto.startTime());
        if (updateDto.endTime() != null)
            timeTable.setEndTime(updateDto.endTime());
        if (updateDto.dayType() != null)
            timeTable.setDayType(updateDto.dayType());
    }
}
