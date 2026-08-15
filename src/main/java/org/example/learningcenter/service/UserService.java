package org.example.learningcenter.service;

import org.example.learningcenter.entity.dto.user.UserCreateDto;
import org.example.learningcenter.entity.dto.user.UserDto;
import org.example.learningcenter.entity.dto.user.UserUpdateDto;
import org.example.learningcenter.entity.model.Organization;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Branch;
import org.example.learningcenter.entity.model.User;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.mapper.UserMapper;
import org.example.learningcenter.repository.BranchRepository;
import org.example.learningcenter.repository.UserRepository;
import org.example.learningcenter.validator.BranchValidator;
import org.example.learningcenter.validator.OrganizationValidator;
import org.example.learningcenter.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<
        UserRepository,
        UserMapper,
        UserValidator> implements CrudService<UserCreateDto, UserUpdateDto, UserDto, String> {

    final BranchRepository branchRepository;
    final BranchValidator branchValidator;
    final OrganizationValidator organizationValidator;

    protected UserService(UserRepository repository, UserMapper mapper, UserValidator validator, BranchRepository branchRepository, BranchValidator branchValidator, OrganizationValidator organizationValidator) {
        super(repository, mapper, validator);
        this.branchRepository = branchRepository;
        this.branchValidator = branchValidator;
        this.organizationValidator = organizationValidator;
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable, String search) {
        Page<User> all = repository.findAll(pageable, search);
        return all.map(mapper::toDto);
    }

    @Override
    public UserDto get(String id) {
        User user = validator.validateIdAndGet(id);
        return mapper.toDto(user);
    }

    @Override
    public UserDto create(UserCreateDto createDto) {
        validator.validate(createDto);
        String organizationId = validator.authenticateAndGetOrganizationId();
        User entity = mapper.toEntity(createDto);
        Branch branch = branchValidator.validateIdAndGet(createDto.branchId());
        Organization organization = organizationValidator.validateAndGetId(organizationId);
        entity.setBranch(branch);
        entity.setOrganization(organization);
        User save = repository.save(entity);
        return mapper.toDto(save);
    }

    @Override
    public UserDto update(UserUpdateDto updateDto, String id) {
        User user = validator.validateIdAndGet(id);
        String organizationId = validator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(user.getOrganization().getId(), organizationId);
        mapper.mapUpdate(user, updateDto);
        return mapper.toDto(repository.save(user));
    }

    @Override
    public void delete(String id) {
        validator.validateId(id);
        String organizationId = validator.authenticateAndGetOrganizationId();
        int rowsUpdated = repository.softDelete(id, organizationId);
        if (rowsUpdated == 0) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Unauthorized);
        }
    }
}
