package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.groupLevel.GroupLevelCreateDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelDto;
import org.example.crm.entity.model.Level;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupLevelMapper {
    public GroupLevelDto toDto(Level level) {
        return new GroupLevelDto(
                level.getId(),
                level.getName(),
                level.getOrderNumber(),
                level.getLessonCount(),
                level.getDurationInMonths()
        );
    }

    public List<GroupLevelDto> toListDto(List<Level> levels) {
        return levels.stream().map(this::toDto).toList();
    }

    public Level toEntity(GroupLevelCreateDto createDto) {
        Level level = new Level();
        level.setName(createDto.name());
        level.setOrderNumber(createDto.orderNumber());
        level.setLessonCount(createDto.lessonCount());
        level.setDurationInMonths(createDto.durationInMonths());
        return level;
    }
}
