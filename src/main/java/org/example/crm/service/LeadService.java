package org.example.crm.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Future;
import org.example.crm.entity.dto.enrollment.EnrollmentCreateDto;
import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadDto;
import org.example.crm.entity.dto.lead.LeadRejectDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.entity.enums.Role;
import org.example.crm.entity.model.Lead;
import org.example.crm.entity.model.Level;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.LeadMapper;
import org.example.crm.projection.LeadProjection;
import org.example.crm.repository.LeadRepository;
import org.example.crm.validator.GroupLevelValidator;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.LeadValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeadService extends AbstractService<
        LeadRepository,
        LeadMapper,
        LeadValidator> implements CrudService<LeadCreateDto, LeadUpdateDto, LeadDto, String> {

    private final UserValidator userValidator;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final GroupValidator groupValidator;
    private final GroupLevelValidator groupLevelValidator;

    protected LeadService(LeadRepository repository, LeadMapper mapper, LeadValidator validator, UserValidator userValidator, EnrollmentService enrollmentService, StudentService studentService, GroupValidator groupValidator, GroupLevelValidator groupLevelValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.groupValidator = groupValidator;
        this.groupLevelValidator = groupLevelValidator;
    }

    @Override
    public Page<LeadDto> getAll(Pageable pageable, String search) {
        return null;
    }

    public Page<LeadDto> getAll(Pageable pageable, String search, LeadStatus status) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        String searchPattern = (search != null && !search.isBlank())
                ? "%" + search.trim().toLowerCase() + "%"
                : null;
        Page<LeadProjection> all = repository.findAll(organizationId, searchPattern, status, pageable);
        Page<LeadDto> map = all.map(mapper::toDto);
        map.getContent().forEach(l -> System.out.println("lead->" + l));
        return map;
    }

    @Override
    public LeadDto get(String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        Lead lead = validator.validateIdAndGet(id);
        return mapper.toDto(lead);
    }

    @Override
    public LeadDto create(LeadCreateDto createDto) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        validator.validate(createDto);
        Lead entity = mapper.toEntity(createDto);
        Level level = groupLevelValidator.validateAndGet(createDto.preferredCourseId());
        entity.setPreferredCourse(level);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public LeadDto update(LeadUpdateDto updateDto, String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        validator.validate(updateDto);
        Lead lead = validator.validateIdAndGet(id);
        mapper.mapUpdate(lead, updateDto);
        Level level = groupLevelValidator.validateAndGet(updateDto.preferredCourseId());
        lead.setPreferredCourse(level);
        Lead save = repository.save(lead);
        return mapper.toDto(save);
    }

    @Override
    public void delete(String id) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        validator.validateId(id);
        Integer integer = repository.softDelete(id);
        if (integer == 0) {
            throw new RestException(ErrorType.LEAD_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    @Transactional
    public LeadDto enroll(String id, String groupId) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        Lead lead = validator.validateIdAndGet(id);
        if (lead.getStatus().equals(LeadStatus.ENROLLED)) {
            throw new RestException(ErrorType.LEAD_ALREADY_ENROLLED, ErrorCodes.BadRequest);
        }
        String branchId = groupValidator.validateIdAndGetBranchId(groupId);
        UserCreateDto userCreateDto = new UserCreateDto(lead.getFullName(), lead.getPhone(), null, Role.STUDENT, branchId, null);
        StudentDto studentDto = studentService.create(new StudentCreateDto(userCreateDto, null));
        enrollmentService.create(new EnrollmentCreateDto(studentDto.id(), groupId));
        lead.setStatus(LeadStatus.ENROLLED);
        Lead save = repository.save(lead);
        return mapper.toDto(save);
    }

    public LeadDto reject(String id, LeadRejectDto dto) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        Lead lead = validator.validateIdAndGet(id);
        if (lead.getStatus().equals(LeadStatus.ENROLLED)) {
            throw new RestException(ErrorType.LEAD_ALREADY_ENROLLED, ErrorCodes.BadRequest);
        }
        lead.setRejectionNote(dto.note());
        lead.setRejectionReason(dto.reason());
        lead.setStatus(LeadStatus.REJECTED);
        Lead save = repository.save(lead);
        return mapper.toDto(save);
    }

    public LeadDto callLater(String id, @Future LocalDateTime callAt) {
        User user = userValidator.authenticateAndGetUser();
        userValidator.validateAdministratorPermission(user, AdministratorPermission.LEAD_MANAGEMENT);
        Lead lead = validator.validateIdAndGet(id);
        if (lead.getStatus().equals(LeadStatus.ENROLLED)||
                lead.getStatus().equals(LeadStatus.REJECTED)){
            throw new RestException(ErrorType.LEAD_STATUS_CHANGE_FORBIDDEN,ErrorCodes.Forbidden);
        }
        lead.setCallAt(callAt);
        lead.setStatus(LeadStatus.CALL_LATER);
        return mapper.toDto(repository.save(lead));
    }
}
