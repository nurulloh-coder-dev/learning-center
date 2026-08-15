package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.branch.BranchCreateDto;
import org.example.crm.entity.dto.branch.BranchDto;
import org.example.crm.entity.dto.branch.BranchUpdateDto;
import org.example.crm.entity.model.Branch;
import org.example.crm.entity.model.Organization;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.BranchMapper;
import org.example.crm.repository.BranchRepository;
import org.example.crm.validator.BranchValidator;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BranchService extends AbstractService<
        BranchRepository,
        BranchMapper,
        BranchValidator> implements CrudService<BranchCreateDto, BranchUpdateDto, BranchDto, String> {

    private final UserValidator userValidator;
    private final OrganizationValidator organizationValidator;

    protected BranchService(BranchRepository repository, BranchMapper mapper, BranchValidator validator, UserValidator userValidator, OrganizationValidator organizationValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.organizationValidator = organizationValidator;
    }

    @Override
    public Page<BranchDto> getAll(Pageable pageable, String search) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Page<Branch> branches = repository.findAll(search, organizationId, pageable);
        return branches.map(mapper::toDto);
    }

    @Override
    public BranchDto get(String id) {
        Branch branch = validator.validateIdAndGet(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(branch.getOrganization().getId(), organizationId);
        return mapper.toDto(branch);
    }

    @Override
    public BranchDto create(BranchCreateDto createDto) {
        validator.validate(createDto.name());
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Organization organization = organizationValidator.validateAndGetId(organizationId);
        Branch branch = mapper.toEntity(createDto, organization);
        Branch save = repository.save(branch);
        return mapper.toDto(save);
    }

    @Override
    @Transactional
    public BranchDto update(BranchUpdateDto updateDto, String id) {
        Branch branch = validator.validateIdAndGet(id);
        validator.validate(updateDto.name());
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(branch.getOrganization().getId(), organizationId);
        mapper.updateEntity(branch, updateDto);
        Branch save = repository.save(branch);
        return mapper.toDto(save);

    }

    @Override
    public void delete(String id) {
        validator.validateId(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        int rowsUpdated = repository.deleteByIdFalse(id, organizationId);
        if (rowsUpdated == 0) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Unauthorized);
        }
    }

    public Long getAllCount(User user) {

        /// add organization filter
        return null;


    }
}
