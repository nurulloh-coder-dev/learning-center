package org.example.crm.service;

import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.entity.model.Lead;
import org.example.crm.mapper.LeadMapper;
import org.example.crm.projection.LeadProjection;
import org.example.crm.repository.LeadRepository;
import org.example.crm.validator.LeadValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LeadService extends AbstractService<
        LeadRepository,
        LeadMapper,
        LeadValidator> implements CrudService<LeadCreateDto, LeadUpdateDto, LeadDto, String> {

    private final UserValidator userValidator;

    protected LeadService(LeadRepository repository, LeadMapper mapper, LeadValidator validator, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
    }

    @Override
    public Page<LeadDto> getAll(Pageable pageable, String search) {
        return null;
    }

    public Page<LeadDto> getAll(Pageable pageable, String search, LeadStatus status) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        String searchPattern = (search != null && !search.isBlank()) ? "%" + search + "%" : null;
        Page<LeadProjection> all = repository.findAll(organizationId, searchPattern, status, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public LeadDto get(String id) {
        Lead lead = validator.validateIdAndGet(id);
        return mapper.toDto(lead);
    }

    @Override
    public LeadDto create(LeadCreateDto createDto) {
        validator.validate(createDto);
        Lead entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public LeadDto update(LeadUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public LeadDto updateStatus(String id, LeadStatus status) {
        return null;
    }
}
