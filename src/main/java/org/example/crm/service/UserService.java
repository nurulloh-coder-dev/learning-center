package org.example.crm.service;

import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.dto.user.UserUpdateDto;
import org.example.crm.entity.model.Organization;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Branch;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.UserMapper;
import org.example.crm.repository.BranchRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.BranchValidator;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.UserValidator;
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
        User entity = mapper.toEntity(createDto);
        validator.validateUserPermission(entity);
        Branch branch = branchValidator.validateIdAndGet(createDto.branchId());
        entity.setBranch(branch);
        User save = repository.save(entity);
        return mapper.toDto(save);
    }

    @Override
    public UserDto update(UserUpdateDto updateDto, String id) {
        User user = validator.authenticateAndGetUser();
        String organizationId = validator.authenticateAndGetOrganizationId();
        validator.validateIfCurrentUser(user, id);
        organizationValidator.validateOrganizationMatch(user.getOrganizationId(), organizationId);
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
