package org.example.crm.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.crm.entity.dto.teacher.TeacherCreateDto;
import org.example.crm.entity.dto.teacher.TeacherDto;
import org.example.crm.entity.dto.teacher.TeacherUpdateDto;
import org.example.crm.entity.model.Teacher;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.TeacherMapper;
import org.example.crm.repository.TeacherRepository;
import org.example.crm.validator.TeacherValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends AbstractService<
        TeacherRepository,
        TeacherMapper,
        TeacherValidator> implements CrudService<TeacherCreateDto, TeacherUpdateDto, TeacherDto,String>{

    final UserValidator userValidator;
    protected TeacherService(TeacherRepository repository, TeacherMapper mapper, TeacherValidator validator, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
    }

    @Override
    public Page<TeacherDto> getAll(Pageable pageable, String search) {
        Page<Teacher> all = repository.findAll(pageable, search);
        return all.map(mapper::toDto);
    }

    @Override
    public TeacherDto get(String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        return mapper.toDto(teacher);
    }

    @Override
    public TeacherDto create(TeacherCreateDto createDto) {
       validator.validate();
        Teacher entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public TeacherDto update(TeacherUpdateDto updateDto, String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        mapper.mapUpdate(teacher,updateDto);
        return mapper.toDto(repository.save(teacher));
    }

    @Override
    public void delete(String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        teacher.setDeleted(true);
        repository.save(teacher);
    }

    public Long getAllCount(String organizationId) {
        return repository.countTeachersByDeletedAndOrg(organizationId);
    }

    public TeacherDto get(String id, String organizationId) {
        Teacher teacher = validator.validateIdAndGetOrg(id,organizationId);
        return mapper.toDto(teacher);
    }

    public TeacherDto create(@Valid TeacherCreateDto createDto, String organizationId) {

        String organization = userValidator.authenticateAndGetOrganizationId();
        if (!organization.equals(organizationId)) {
            throw  new RestException(ErrorType.ORGANIZATION_ID_MISMATCH, ErrorCodes.BadRequest);
        }
        Teacher entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public TeacherDto update(@Valid TeacherUpdateDto updateDto, String id, String organizationId) {
        Teacher teacher = validator.validateIdAndGetOrg(id, organizationId);
        mapper.mapUpdate(teacher, updateDto);
        return mapper.toDto(repository.save(teacher));
    }

    @Transactional
    public void delete(String id, String organizationId) {
        Teacher teacher = validator.validateIdAndGetOrg(id, organizationId);
        teacher.setDeleted(true);
        repository.save(teacher);
    }
}
