package org.example.crm.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.teacher.TeacherCreateDto;
import org.example.crm.entity.dto.teacher.TeacherCreateResponseDto;
import org.example.crm.entity.dto.teacher.TeacherDto;
import org.example.crm.entity.dto.teacher.TeacherUpdateDto;
import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.dto.user.UserCreatedResponseDto;
import org.example.crm.entity.enums.Role;
import org.example.crm.entity.model.Teacher;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.TeacherFilterDto;
import org.example.crm.mapper.TeacherMapper;
import org.example.crm.repository.TeacherRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.TeacherValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TeacherService extends AbstractService<
        TeacherRepository,
        TeacherMapper,
        TeacherValidator> implements CrudService<TeacherFilterDto, TeacherCreateDto, TeacherUpdateDto, TeacherDto, String, Page<TeacherDto>> {

    final UserValidator userValidator;
    final UserRepository userRepository;
    final StudentService studentService;
    final UserService userService;

    protected TeacherService(TeacherRepository repository, TeacherMapper mapper, TeacherValidator validator, UserValidator userValidator, UserRepository userRepository, StudentService studentService, UserService userService) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.userService = userService;
    }

    @Override
    public Page<TeacherDto> getAll(Pageable pageable, TeacherFilterDto filterDto) {

        String organizationId = userValidator.authenticateAndGetOrganizationId();
        log.info("orgId of current user {}", organizationId);
        Page<Teacher> all = repository.findAllBySearch(organizationId, filterDto.search(), pageable);
        System.out.println(all);
        return all.map(mapper::toDto);
    }

    @Override
    public TeacherDto get(String id) {


        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Teacher teacher = validator.validateIdAndGetOrg(id, organizationId);

        return mapper.toDto(teacher);
    }

    @Override
    public TeacherDto create(TeacherCreateDto createDto) {
        return null;
    }

    @Transactional
    public TeacherCreateResponseDto createTeacher(TeacherCreateDto createDto) {
        validator.validate(createDto);

        UserCreatedResponseDto userResponse = userService.createUser(new UserCreateDto(
                createDto.user().fullName(),
                createDto.user().phone(),
                createDto.user().birthDate(),
                Role.TEACHER,
                createDto.user().branchId(),
                null
        ));

        User user = userRepository.getReferenceById(userResponse.id());

        Teacher teacher = mapper.toEntity(createDto);
        teacher.setUser(user);

        Teacher savedTeacher = repository.save(teacher);

        return new TeacherCreateResponseDto(
                savedTeacher.getId(),
                userResponse,
                savedTeacher.getTotalTeachingExp(),
                savedTeacher.getCurrPlaceTeachingExp()
        );
    }

    @Override
    public TeacherDto update(TeacherUpdateDto updateDto, String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        mapper.mapUpdate(teacher, updateDto);
        return mapper.toDto(repository.save(teacher));
    }

    @Override
    @Transactional
    public void delete(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Teacher teacher = validator.validateIdAndGetOrg(id, organizationId);
        repository.softDelete(teacher.getId());
        userService.softDeleteUserAndOrganization(teacher.getUser(), organizationId);
    }

    public Long getAllCount() {


        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countTeachersByDeletedAndOrg(organizationId);
    }
}
