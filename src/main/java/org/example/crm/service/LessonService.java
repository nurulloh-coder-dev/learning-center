package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.lesson.LessonCreateDto;
import org.example.crm.entity.dto.lesson.LessonDto;
import org.example.crm.entity.dto.lesson.LessonUpdateDto;
import org.example.crm.entity.enums.EnrollmentPaymentStatus;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.*;
import org.example.crm.mapper.LessonMapper;
import org.example.crm.repository.*;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.LessonValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LessonService extends AbstractService<
        LessonRepository,
        LessonMapper,
        LessonValidator> implements CrudService<LessonCreateDto, LessonUpdateDto, LessonDto, String> {
    final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final UserValidator userValidator;
    final GroupLevelRepository groupLevelRepository;
    private final GroupValidator groupValidator;
    private final EnrollmentRepository enrollmentRepository;
    private final InvoiceService invoiceService;


    protected LessonService(LessonRepository repository, LessonMapper mapper, LessonValidator validator, TeacherRepository teacherRepository, GroupRepository groupRepository, UserValidator userValidator, GroupLevelRepository groupLevelRepository, GroupValidator groupValidator, EnrollmentRepository enrollmentRepository, InvoiceService invoiceService) {
        super(repository, mapper, validator);
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.userValidator = userValidator;
        this.groupLevelRepository = groupLevelRepository;
        this.groupValidator = groupValidator;
        this.enrollmentRepository = enrollmentRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public Page<LessonDto> getAll(Pageable pageable, String search) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Page<Lesson> all = repository.findAll(pageable,organizationId, search);
        return all.map(mapper::toDto);
    }

    @Override
    public LessonDto get(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Lesson lesson = validator.validateIdAndGet(id,organizationId);
        return mapper.toDto(lesson);
    }

    @Override
    @Transactional
    public LessonDto create(LessonCreateDto createDto) {
        validator.validate(createDto);
        Group group = groupValidator.validateIdAndGet(createDto.groupId());
        Level level = group.getLevel();
        Integer lessonsInCurrMonth = repository.findLessonCountByGroupId(group.getId(), level.getName()).orElse(0) + 1;
        Lesson entity = toEntity(createDto,String.format("%s.%s",group.getCurrentMonth(),lessonsInCurrMonth),group);
        Lesson save = repository.save(entity);
        group.registerCompletedLesson(lessonsInCurrMonth, groupLevelRepository);
        groupRepository.save(group);
        if (!group.getStatus().equals(GroupStatus.COMPLETED)){
            invoiceService.createGroupInvoice(group.getId());
        }
        return mapper.toDto(save);
    }

    private Lesson toEntity(LessonCreateDto createDto, String title,Group group) {
        return new Lesson(
                title,
                createDto.topic(),
                false,
                group,
                group.getTeacher(),
                group.getLevel()
        );
    }


    @Override
    public LessonDto update(LessonUpdateDto updateDto, String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Lesson lesson = validator.validateIdAndGet(id,organizationId);
        mapper.mapUpdate(lesson, updateDto);
        Lesson save = repository.save(lesson);
        return mapper.toDto(save);
    }

    @Override
    public void delete(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Lesson lesson = validator.validateIdAndGet(id,organizationId);
        lesson.setDeleted(true);
        repository.save(lesson);
    }

    public Long getAllCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countLessonsByOrgIdAndDeleted(organizationId);
    }


    public Integer getLessonCountByGroup(String groupId, String name) {
        groupValidator.validateIdAndGet(groupId);
        return repository.findLessonCountByGroupId(groupId,name).orElse(0);
    }
}
