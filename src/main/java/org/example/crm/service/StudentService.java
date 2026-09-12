package org.example.crm.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.student.StudentUpdateDto;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.StudentFilterDto;
import org.example.crm.mapper.StudentMapper;
import org.example.crm.mapper.UserMapper;
import org.example.crm.projection.StudentProjection;
import org.example.crm.projection.StudentShowProjection;
import org.example.crm.repository.EnrollmentRepository;
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
        StudentValidator> implements CrudService<StudentFilterDto, StudentCreateDto, StudentUpdateDto, StudentDto, String, Page<StudentDto>> {

    final UserService userService;
    private final UserValidator userValidator;
    final EnrollmentRepository enrollmentRepository;
    final UserMapper userMapper;

    protected StudentService(StudentRepository repository, StudentMapper mapper, StudentValidator validator, UserService userService, UserValidator userValidator, EnrollmentRepository enrollmentRepository, UserMapper userMapper) {
        super(repository, mapper, validator);
        this.userService = userService;
        this.userValidator = userValidator;
        this.enrollmentRepository = enrollmentRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<StudentDto> getAll(Pageable pageable, StudentFilterDto filterDto) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        log.info("org id of current user {}", organizationId);
        Page<StudentProjection> all = repository.searchStudentsByOrganization(filterDto.search(), organizationId, pageable);
        System.out.println(all);
        return all.map(mapper::toDtoProj);
    }

    @Override
    public StudentDto get(String id) {


        Student student = validator.validateIdAndGet(id);
        return mapper.toDto(student);
    }

    @Override
    public StudentDto create(StudentCreateDto createDto) {


        validator.validate(createDto);
        Student entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StudentDto update(StudentUpdateDto updateDto, String id) {


        Student student = validator.validateIdAndGet(id);
        mapper.mapUpdate(student, updateDto);
        return mapper.toDto(repository.save(student));
    }

    @Override
    public void delete(String id) {
        validator.validateId(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        repository.softDelete(id, organizationId);
    }

    public Long getAllCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countStudentsByOrganizationId(organizationId);
    }

    public List<StudentDto> getStudentsByGroupId(String groupId) {
        List<StudentShowProjection> studentByGroupId = repository.getStudentShowByGroupId(groupId);
        return studentByGroupId
                .stream()
                .map(mapper::toDtoShowProj)
                .toList();
    }

    public List<StudentDto> getByPhone(String phone) {
        List<Student> studentsByPhone = repository.getStudentByPhone(phone);
        return studentsByPhone
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public StudentDto getMe() {
        User userId = userValidator.authenticateAndGetUser();
        Student student = validator.validateStudentByUserId(userId.getId());
        return new StudentDto(
                student.getId(),
                userMapper.toDto(student.getUser()),
                student.getParentPhone(),
                student.getBalance()
        );
    }

    public Invoice getLatestInvoice(@NotNull String studentId) {
        return repository.findLatestInvoiceByStudentId(studentId)
                .orElseThrow(() -> new RestException(ErrorType.INVOICE_NOT_FOUND, ErrorCodes.NotFound));

    }
}
