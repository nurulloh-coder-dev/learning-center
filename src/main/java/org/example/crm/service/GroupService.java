package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.group.FullGroupDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.enums.DayType;
import org.example.crm.entity.model.Level;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.TimeTable;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.RestException;
import org.example.crm.projection.GroupNameProjection;
import org.example.crm.projection.GroupProjection;
import org.example.crm.entity.dto.group.GroupDto;
import org.example.crm.entity.dto.group.GroupCreateDto;
import org.example.crm.entity.dto.group.GroupUpdateDto;
import org.example.crm.entity.model.Group;
import org.example.crm.mapper.GroupMapper;
import org.example.crm.repository.GroupRepository;
import org.example.crm.repository.LessonRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupService extends AbstractService<
        GroupRepository,
        GroupMapper,
        GroupValidator> implements CrudService<GroupCreateDto, GroupUpdateDto, GroupDto, String> {

    private final UserValidator userValidator;
    private final StudentService studentService;
    private final LessonRepository lessonRepository;
    final UserRepository userRepository;

    protected GroupService(GroupRepository repository, GroupMapper mapper, GroupValidator validator, UserValidator userValidator, StudentService studentService, LessonRepository lessonRepository, UserRepository userRepository) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.studentService = studentService;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<GroupDto> getAll(Pageable pageable, String search) {
//        Page<GroupProjection> projectionPage = repository.getAllByFilter(search, pageable);
//        return projectionPage.
//                map(mapper::toDtoFromProjection);
        return null;
    }

    public Page<GroupDto> getAll(Pageable pageable, String search, GroupStatus status, String level) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();

        String searchPattern = (search != null && !search.isBlank())
                ? "%" + search.trim().toLowerCase() + "%"
                : null;

        Page<GroupProjection> groups = repository.getAllByFilter(
                organizationId,
                status,
                level,
                searchPattern,
                pageable
        );

        return groups.map(mapper::toDtoFromProjection);
    }

    @Override
    public GroupDto get(String id) {
        Group group = validator.validateIdAndGet(id);
        Integer lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        return mapper.toDto(group, lessonsCount);
    }
    public GroupDto get(String id, String organizationId) {
        Group group = validator.validateIdOrgAndGet(id,organizationId);
        Integer lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        return mapper.toDto(group, lessonsCount);
    }

    @Override
    public GroupDto create(GroupCreateDto createDto) {
        validator.createValid(createDto);
        String s = userValidator.authenticateAndGetId();
        User currentUser = userRepository.findByIdAndDeletedFalse(s).orElseThrow(() -> new RestException(ErrorType.USER_NOT_FOUND, ErrorCodes.NotFound));
        Group group = mapper.toEntity(createDto, currentUser.getBranch());

        Integer lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        return mapper.toDto(repository.save(group), lessonsCount);
    }

    @Override
    public GroupDto update(GroupUpdateDto updateDto, String id) {
        Group group = validator.validateIdAndGet(id);
        mapper.mapUpdate(group, updateDto);
        Integer lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        return mapper.toDto(repository.save(group), lessonsCount);
    }

    public GroupDto update(GroupUpdateDto updateDto, String id, String organizationId) {
        Group group = validator.validateIdOrgAndGet(id, organizationId);
        mapper.mapUpdate(group, updateDto);
        Integer lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        return mapper.toDto(repository.save(group), lessonsCount);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Group group = validator.validateIdAndGet(id);
        repository.updateDeleted(group.getId());

    }

    public Integer getCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Optional<Integer> count = repository.getCount(organizationId);
        return count.orElse(0);
    }

    public List<GroupNameProjection> getGroupNames() {
        String userId = userValidator.authenticateAndGetId();
        return repository.findAllGroupNames(userId);
    }

    public FullGroupDto getGroupInfo(String groupId) {
        GroupDto groupDto;
        Group group;
        List<StudentDto> studentsByGroupId;
        Integer lessonsCount;
        if (groupId == null) {
            String userId = userValidator.authenticateAndGetId();
            List<Group> allByTeacherId = repository.findAllByTeacherUserId(userId);
            if (allByTeacherId.isEmpty()) {
                return null;
            }
            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
            group = calculateTimeTable(dayOfWeek, allByTeacherId);
            if (group == null) {
                // try tomorrow
                group = calculateTimeTable(LocalDate.now().plusDays(1).getDayOfWeek(), allByTeacherId);
            }
            if (group == null) {
                return null;
            }
            lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        } else {
            group = validator.validateIdAndGet(groupId);
            lessonsCount = lessonRepository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0);
        }
        groupDto = mapper.toDto(group, lessonsCount);
        studentsByGroupId = studentService.getStudentsByGroupId(groupDto.id());
        return new FullGroupDto(studentsByGroupId, groupDto);
    }

    private Group calculateTimeTable(DayOfWeek dayOfWeek, List<Group> allByTeacherId) {
        Group nearestGroup = null;
        for (Group group : allByTeacherId) {
            TimeTable timeTable = group.getTimeTable();
            if (Objects.equals(isOddOrEvenDayOfWeek(dayOfWeek), timeTable.getDayType())) {
                LocalTime startTime = timeTable.getStartTime();
                LocalTime now = LocalTime.now();
                if (startTime.isAfter(now)) {
                    if (nearestGroup == null) {
                        nearestGroup = group;
                    } else {
                        Duration newDiff = Duration.between(startTime, now);
                        Duration currentDiff = Duration.between(nearestGroup.getTimeTable().getStartTime(), now);
                        if (newDiff.compareTo(currentDiff) < 0) nearestGroup = group;
                    }
                }
            }
        }
        return nearestGroup;
    }

    public DayType isOddOrEvenDayOfWeek(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue() % 2 != 0 ? DayType.ODD : DayType.EVEN;
    }
}
