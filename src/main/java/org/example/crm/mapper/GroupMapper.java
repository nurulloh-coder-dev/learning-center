package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.group.GroupOverviewDto;
import org.example.crm.entity.dto.teacher.TeacherIdNameDto;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.Branch;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.projection.GroupProjection;
import org.example.crm.entity.dto.group.GroupCreateDto;
import org.example.crm.entity.dto.group.GroupDto;
import org.example.crm.entity.dto.group.GroupUpdateDto;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Group;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.GroupLevelRepository;
import org.example.crm.repository.TeacherRepository;
import org.example.crm.repository.TimeTableRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    final TeacherRepository teacherRepository;
    final TimeTableRepository timeTableRepository;
    final TeacherMapper teacherMapper;
    final TimeTableMapper timeTableMapper;
    final GroupLevelRepository groupLevelRepository;

    public Group toEntity(GroupCreateDto createDto, Branch branch) {
        return new Group(
                createDto.name(),
                createDto.room(),
                createDto.startDate(),
                teacherRepository.findById(createDto.teacherId())
                        .orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound)),
                timeTableRepository.save(timeTableMapper.toEntity(createDto.timeTable())),
                GroupStatus.STARTING,
                groupLevelRepository.getFirstLevelForGroup(branch.getOrganizationId())
                        .orElseThrow(() -> new RestException(ErrorType.GROUP_LEVEL_NOT_FOUND, ErrorCodes.NotFound)),
                branch,
                1

        );
    }

    public GroupDto toDto(Group save) {
        return new GroupDto(
                save.getId(),
                save.getName(),
                save.getRoom(),
                save.getStartDate(),
                teacherMapper.toDto(save.getTeacher()),
                timeTableMapper.toDto(save.getTimeTable()),
                save.getStatus(),
                save.getLevel().getName(),
                save.getCurrentMonth()
        );
    }

    public GroupOverviewDto toDtoFromProjection(GroupProjection projection) {
        return new GroupOverviewDto(
                projection.getId(),
                projection.getName(),
                projection.getRoom(),
                projection.getStartDate(),
                projection.getStatus(),
                projection.getLevelName(),
                new TeacherIdNameDto(projection.getTeacherId(), projection.getTeacherFullName()),
                timeTableMapper.toDto(projection.getTimeTable()),
                projection.getCurrentMonth(),
                projection.getLessonsCount(),
                projection.getStudentCount()
        );
    }

    public void mapUpdate(Group group, GroupUpdateDto updateDto) {
        if (updateDto.teacherId() != null)
            group.setTeacher(teacherRepository.findById(updateDto.teacherId()).orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound)));
        group.setStartDate(updateDto.startDate());
        group.setRoom(updateDto.room());
        if (updateDto.timeTable() != null) {
            timeTableMapper.update(group.getTimeTable(), updateDto.timeTable());
            timeTableRepository.save(group.getTimeTable());
        }
    }
}
