package org.example.crm.service;

import org.example.crm.entity.dto.enrollment.EnrollmentCreateDto;
import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.dto.enrollment.EnrollmentUpdateDto;
import org.example.crm.entity.model.Enrollment;
import org.example.crm.entity.model.Group;
import org.example.crm.entity.model.Student;
import org.example.crm.mapper.EnrollmentMapper;
import org.example.crm.repository.EnrollmentRepository;
import org.example.crm.repository.StudentRepository;
import org.example.crm.validator.EnrollmentValidator;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.StudentValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService extends AbstractService<
        EnrollmentRepository,
        EnrollmentMapper,
        EnrollmentValidator> implements CrudService<EnrollmentCreateDto, EnrollmentUpdateDto, EnrollmentDto, String> {

    private final StudentValidator studentValidator;
    private final GroupValidator groupValidator;
    private final StudentRepository studentRepository;
    private final UserValidator userValidator;

    protected EnrollmentService(EnrollmentRepository repository, EnrollmentMapper mapper, EnrollmentValidator validator, StudentValidator studentValidator, GroupValidator groupValidator, StudentRepository studentRepository, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.studentValidator = studentValidator;
        this.groupValidator = groupValidator;
        this.studentRepository = studentRepository;
        this.userValidator = userValidator;
    }

    @Override
    public Page<EnrollmentDto> getAll(Pageable pageable, String search) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Page<Enrollment> allBySearch = repository.findAllBySearch(organizationId, search, pageable);
        return allBySearch.map(mapper::toDto);
    }

    public Page<EnrollmentDto> getAll(Pageable pageable, String search, String groupId) {
        Page<Enrollment> allBySearch = repository.findAllBySearch(groupId, pageable, search);
        return allBySearch.map(mapper::toDto);
    }

    @Override
    public EnrollmentDto get(String id) {
        Enrollment enrollment = validator.validateIdAndGet(id);
        return mapper.toDto(enrollment);
    }

    @Override
    public EnrollmentDto create(EnrollmentCreateDto createDto) {
        Student student = studentValidator.validateIdAndGet(createDto.studentId());
        Group group = groupValidator.validateIdAndGet(createDto.groupId());
        student.setBalance(student.getBalance().subtract(group.getLevel().getMonthlyFee()));
        studentRepository.save(student);
        Enrollment enrollment = new Enrollment(student, group, null);
        return mapper.toDto(repository.save(enrollment));
    }

    @Override
    public EnrollmentDto update(EnrollmentUpdateDto updateDto, String id) {
        Enrollment enrollment = validator.validateIdAndGet(id);
        Student student = studentValidator.validateIdAndGet(updateDto.studentId());
        Group group = groupValidator.validateIdAndGet(updateDto.groupId());
        enrollment.setGroup(group);
        enrollment.setStudent(student);
        return mapper.toDto(repository.save(enrollment));
    }

    @Override
    public void delete(String id) {

    }

    public void delete(String id, String reason) {
        validator.validateId(id);
        repository.softDelete(reason, id);
    }
}
