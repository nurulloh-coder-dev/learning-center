package org.example.crm.service;

import lombok.extern.slf4j.Slf4j;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.student.StudentUpdateDto;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.User;
import org.example.crm.mapper.StudentMapper;
import org.example.crm.projection.StudentProjection;
import org.example.crm.projection.StudentShowProjection;
import org.example.crm.repository.StudentRepository;
import org.example.crm.validator.StudentValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService extends AbstractService<
        StudentRepository,
        StudentMapper,
        StudentValidator> implements CrudService<StudentCreateDto, StudentUpdateDto, StudentDto, String> {

    final UserService userService;
    private final UserValidator userValidator;

    protected StudentService(StudentRepository repository, StudentMapper mapper, StudentValidator validator, UserService userService, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @Override
    public Page<StudentDto> getAll(Pageable pageable, String search) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);

        String organizationId = userValidator.authenticateAndGetOrganizationId();
        log.info("org id of current user {}", organizationId);
        Page<StudentProjection> all = repository.searchStudentsByOrganization(search, organizationId, pageable);
        System.out.println(all);
        return all.map(mapper::toDtoProj);
    }

    @Override
    public StudentDto get(String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        Student student = validator.validateIdAndGet(id);
        return mapper.toDto(student);
    }

    @Override
    public StudentDto create(StudentCreateDto createDto) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        validator.validate(createDto);
        Student entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StudentDto update(StudentUpdateDto updateDto, String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        Student student = validator.validateIdAndGet(id);
        mapper.mapUpdate(student, updateDto);
        return mapper.toDto(repository.save(student));
    }

    @Override
    public void delete(String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        Student student = validator.validateIdAndGet(id);
        student.setDeleted(true);
        repository.save(student);
    }

    public Long getAllCount() {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countStudentsByOrganizationId(organizationId);
    }

    public List<StudentDto> getStudentsByGroupId(String groupId) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        List<StudentShowProjection> studentByGroupId = repository.getStudentShowByGroupId(groupId);
        return studentByGroupId
                .stream()
                .map(mapper::toDtoShowProj)
                .toList();
    }

    public List<StudentDto> getByPhone(String phone) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.STUDENT_MANAGEMENT);


        List<Student> studentsByPhone = repository.getStudentByPhone(phone);
        return studentsByPhone
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
