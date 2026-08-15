package org.example.learningcenter.service;


import org.example.learningcenter.entity.dto.organization.OrganizationCreateDto;
import org.example.learningcenter.entity.dto.organization.OrganizationDto;
import org.example.learningcenter.entity.dto.organization.OrganizationUpdateDto;
import org.example.learningcenter.entity.model.Organization;
import org.example.learningcenter.mapper.OrganizationMapper;
import org.example.learningcenter.repository.OrganizationRepository;
import org.example.learningcenter.validator.OrganizationValidator;
import org.example.learningcenter.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends AbstractService<
        OrganizationRepository,
        OrganizationMapper,
        OrganizationValidator> implements CrudService<OrganizationCreateDto, OrganizationUpdateDto, OrganizationDto, String> {
    private final UserValidator userValidator;

    protected OrganizationService(OrganizationRepository repository, OrganizationMapper mapper, OrganizationValidator validator, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
    }

    @Override
    public Page<OrganizationDto> getAll(Pageable pageable, String search) {
        Page<Organization> all = repository.findAll(search, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public OrganizationDto get(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        validator.validateOrganizationMatch(id, organizationId);
        Organization organization = validator.validateAndGetId(id);
        return mapper.toDto(organization);
    }

    @Override
    public OrganizationDto create(OrganizationCreateDto createDto) {
        validator.validate(createDto);
        Organization entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public OrganizationDto update(OrganizationUpdateDto updateDto, String id) {
        validator.validate(updateDto);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        validator.validateOrganizationMatch(id,organizationId);
        Organization organization = validator.validateAndGetId(id);
        mapper.mapUpdate(organization, updateDto);
        return mapper.toDto(repository.save(organization));
    }

    @Override
    public void delete(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        validator.validateOrganizationMatch(id, organizationId);
        repository.softDelete(id);
    }
}
