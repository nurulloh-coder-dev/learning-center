package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.lesson.LessonCreateDto;
import org.example.crm.entity.dto.lesson.LessonDto;
import org.example.crm.entity.dto.lesson.LessonUpdateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Group;
import org.example.crm.entity.model.Lesson;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.LessonMapper;
import org.example.crm.repository.GroupLevelRepository;
import org.example.crm.repository.GroupRepository;
import org.example.crm.repository.TeacherRepository;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.LessonValidator;
import org.example.crm.repository.LessonRepository;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    protected LessonService(LessonRepository repository, LessonMapper mapper, LessonValidator validator, TeacherRepository teacherRepository, GroupRepository groupRepository, UserValidator userValidator, GroupLevelRepository groupLevelRepository, GroupValidator groupValidator) {
        super(repository, mapper, validator);
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.userValidator = userValidator;
        this.groupLevelRepository = groupLevelRepository;
        this.groupValidator = groupValidator;
    }

    @Override
    public Page<LessonDto> getAll(Pageable pageable, String search) {
        Page<Lesson> all = repository.findAll(pageable, search);
        return all.map(mapper::toDto);
    }

    @Override
    public LessonDto get(String id) {
        Lesson lesson = validator.validateIdAndGet(id);
        return mapper.toDto(lesson);
    }

    @Override
    @Transactional
    public LessonDto create(LessonCreateDto createDto) {
        validator.validate(createDto);
        Group group = groupValidator.validateIdAndGet(createDto.groupId());
        Integer lessonsInCurrMonth = repository.findLessonCountByGroupId(group.getId(), group.getLevel().getName()).orElse(0) + 1;
        group.registerCompletedLesson(lessonsInCurrMonth, groupLevelRepository);
        Lesson entity = toEntity(createDto,String.format("%s.%s",group.getCurrentMonth(),lessonsInCurrMonth),group);
        Lesson save = repository.save(entity);
        groupRepository.save(group);
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
        Lesson lesson = validator.validateIdAndGet(id);
        mapper.mapUpdate(lesson, updateDto);
        Lesson save = repository.save(lesson);
        return mapper.toDto(save);
    }

    @Override
    public void delete(String id) {
        Lesson lesson = validator.validateIdAndGet(id);
        lesson.setDeleted(true);
        repository.save(lesson);
    }

    public Long getAllCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countLessonsByOrgIdAndDeleted(organizationId);
    }


}
