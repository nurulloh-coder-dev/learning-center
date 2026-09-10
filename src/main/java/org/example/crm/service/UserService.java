package org.example.crm.service;

import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.dto.user.UserCreatedResponseDto;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.dto.user.UserUpdateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Branch;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.UserFilterDto;
import org.example.crm.mapper.UserMapper;
import org.example.crm.repository.BranchRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.BranchValidator;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserService extends AbstractService<
        UserRepository,
        UserMapper,
        UserValidator> implements CrudService<UserFilterDto, UserCreateDto, UserUpdateDto, UserDto, String, Page<UserDto>> {

    final BranchRepository branchRepository;
    final BranchValidator branchValidator;
    final OrganizationValidator organizationValidator;
    final PasswordEncoder passwordEncoder;

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    protected UserService(UserRepository repository, UserMapper mapper, UserValidator validator, BranchRepository branchRepository, BranchValidator branchValidator, OrganizationValidator organizationValidator, PasswordEncoder passwordEncoder) {
        super(repository, mapper, validator);
        this.branchRepository = branchRepository;
        this.branchValidator = branchValidator;
        this.organizationValidator = organizationValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable, UserFilterDto filterDto) {
        Page<User> all = repository.findAll(pageable, filterDto.search());
        return all.map(mapper::toDto);
    }

    @Override
    public UserDto get(String id) {
        User user = validator.validateIdAndGet(id);
        return mapper.toDto(user);
    }

    @Override
    public UserDto create(UserCreateDto createDto) {
        return null;
    }


    public UserCreatedResponseDto createUser(UserCreateDto createDto) {
        validator.validate(createDto);
        User entity = mapper.toEntity(createDto);
        validator.validateUserPermission(entity);
        String password = generatePassword(10);
        entity.setPassword(passwordEncoder.encode(password));
        Branch branch = branchValidator.validateIdAndGet(createDto.branchId());
        entity.setBranch(branch);
        User save = repository.save(entity);
        return new UserCreatedResponseDto(
                save.getId(),
                save.getFullName(),
                save.getPhone(),
                password
        );
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

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
