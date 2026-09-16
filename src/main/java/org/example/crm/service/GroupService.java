package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.config.JwtUtils;
import org.example.crm.entity.dto.group.*;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.enums.DayType;
import org.example.crm.entity.model.*;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.GroupFilterDto;
import org.example.crm.projection.GroupNameProjection;
import org.example.crm.projection.GroupProjection;
import org.example.crm.mapper.GroupMapper;
import org.example.crm.projection.GroupStatsProjection;
import org.example.crm.repository.*;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@Service
public class GroupService extends AbstractService<
        GroupRepository,
        GroupMapper,
        GroupValidator> implements CrudService<GroupFilterDto, GroupCreateDto, GroupUpdateDto, GroupDto, String, Page<GroupOverviewDto>> {

    private final UserValidator userValidator;
    private final StudentService studentService;
    final UserRepository userRepository;
    private final UserOrganizationRepository userOrganizationRepository;
    final TeacherRepository teacherRepository;

    protected GroupService(GroupRepository repository, GroupMapper mapper, GroupValidator validator, UserValidator userValidator, StudentService studentService, LessonRepository lessonRepository, UserRepository userRepository, UserOrganizationRepository userOrganizationRepository, TeacherRepository teacherRepository) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.studentService = studentService;
        this.userRepository = userRepository;
        this.userOrganizationRepository = userOrganizationRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Page<GroupOverviewDto> getAll(Pageable pageable, GroupFilterDto filterDto) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();

        String searchPattern = (filterDto.search() != null && !filterDto.search().isBlank())
                ? "%" + filterDto.search().trim().toLowerCase() + "%"
                : null;

        Page<GroupProjection> groups = repository.getAllByFilter(
                organizationId,
                filterDto.status(),
                filterDto.level(),
                searchPattern,
                pageable
        );

        return groups.map(mapper::toDtoFromProjection);
    }

    @Override
    public GroupDto get(String id) {
        Group group = validator.validateIdAndGet(id);
        return mapper.toDto(group);
    }

    @Override
    public GroupDto create(GroupCreateDto createDto) {
        validator.createValid(createDto);
        String userId = userValidator.authenticateAndGetId();
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        UserOrganization userOrganization = userOrganizationRepository.findByUserIdAndOrgId(userId, organizationId)
                .orElseThrow(() -> new RestException(ErrorType.USER_ORGANIZATION_MISMATCH, ErrorCodes.AccessDenied));
        Group group = mapper.toEntity(createDto, userOrganization.getBranch());
        return mapper.toDto(repository.save(group));
    }

    @Override
    public GroupDto update(GroupUpdateDto updateDto, String id) {
        Group group = validator.validateIdAndGet(id);
        mapper.mapUpdate(group, updateDto);
        return mapper.toDto(repository.save(group));
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
        } else {
            group = validator.validateIdAndGet(groupId);
        }
        groupDto = mapper.toDto(group);
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

    public List<GroupDto> getMyGroups() {
        String userId = userValidator.authenticateAndGetId();
        List<Group> myGroups = repository.getMyGroups(userId);
        return myGroups.stream()
                .map(mapper::toDto)
                .toList();
    }

    public GroupStatsDto getGroupStats() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        User user = userValidator.authenticateAndGetUser();
        Teacher teacher = teacherRepository.findTeacherByIdAndOrg(user.getId(), organizationId)
                .orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound));
        List<Group> allGroupsByTeacherId = repository.findAllGroupsByTeacherId(user.getId(), organizationId);
        List<String> groupIdList = allGroupsByTeacherId.stream()
                .map(Group::getId)
                .toList();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthAgo = now.minusMonths(1);

        if (groupIdList.isEmpty()){
            return new GroupStatsDto(0L,0L,0L,0L,0L,0L,0L);
        }
        GroupStatsProjection projection = repository.getGroupStats(groupIdList,monthAgo,now);

        return new GroupStatsDto(
                projection.getTotalStudents(),
                projection.getActiveStudents(),
                projection.getNewStudents(),
                projection.getLostStudents(),
                projection.getPotentialFailStudents(),
                projection.getRedList(),
                projection.getBlackList()
        );
    }
}
