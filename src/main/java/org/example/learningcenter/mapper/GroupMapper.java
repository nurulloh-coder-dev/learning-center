package org.example.learningcenter.mapper;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.enums.GroupLevel;
import org.example.learningcenter.entity.enums.GroupStatus;
import org.example.learningcenter.entity.model.Branch;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.projection.GroupProjection;
import org.example.learningcenter.entity.dto.group.GroupCreateDto;
import org.example.learningcenter.entity.dto.group.GroupDto;
import org.example.learningcenter.entity.dto.group.GroupUpdateDto;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Group;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.TeacherRepository;
import org.example.learningcenter.repository.TimeTableRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    final TeacherRepository teacherRepository;
    final TimeTableRepository timeTableRepository;
    final TeacherMapper teacherMapper;
    final TimeTableMapper timeTableMapper;

    public Group toEntity(GroupCreateDto createDto, Branch branch) {
        return new Group(
                createDto.name(),
                createDto.room(),
                teacherRepository.findById(createDto.teacherId()).orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound)),
                timeTableRepository.save(timeTableMapper.toEntity(createDto.timeTable())),
                GroupStatus.STARTING,
                GroupLevel.A1,
                branch,
                1

        );
    }

    public GroupDto toDto(Group save,Integer lessonsCount) {
        return new GroupDto(
                save.getId(),
                save.getName(),
                save.getRoom(),
                teacherMapper.toDto(save.getTeacher()),
                timeTableMapper.toDto(save.getTimeTable()),
                save.getStatus(),
                save.getLevel(),
                save.getCurrentMonth(),
                lessonsCount
        );
    }

    public GroupDto toDtoFromProjection(GroupProjection projection) {
        return new GroupDto(
                projection.getId(),
                projection.getName(),
                projection.getRoom(),
                teacherMapper.toDto(projection.getTeacher()),
                timeTableMapper.toDto(projection.getTimeTable()),
                projection.getStatus(),
                projection.getLevel(),
                projection.getCurrentMonth(),
                projection.getLessonsCount()
        );
    }

    public void mapUpdate(Group group, GroupUpdateDto updateDto) {
        if (updateDto.teacherId() != null)
            group.setTeacher(teacherRepository.findById(updateDto.teacherId()).orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound)));
        timeTableMapper.update(group.getTimeTable(), updateDto.timeTable());
        timeTableRepository.save(group.getTimeTable());
    }
}
