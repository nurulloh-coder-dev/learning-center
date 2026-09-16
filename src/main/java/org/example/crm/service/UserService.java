package org.example.crm.service;

import org.example.crm.entity.dto.user.*;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.enums.Role;
import org.example.crm.entity.model.Organization;
import org.example.crm.entity.model.UserOrganization;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.UserFilterDto;
import org.example.crm.mapper.UserMapper;
import org.example.crm.mapper.UserOrganizationMapper;
import org.example.crm.repository.BranchRepository;
import org.example.crm.repository.UserOrganizationRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.BranchValidator;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService<
        UserRepository,
        UserMapper,
        UserValidator> implements CrudService<UserFilterDto, UserCreateDto, UserUpdateDto, UserDto, String, Page<UserDto>> {

    final BranchRepository branchRepository;
    final BranchValidator branchValidator;
    final OrganizationValidator organizationValidator;
    final PasswordEncoder passwordEncoder;
    final UserOrganizationRepository userOrganizationRepository;
    final UserOrganizationMapper userOrganizationMapper;

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    protected UserService(UserRepository repository, UserMapper mapper, UserValidator validator, BranchRepository branchRepository, BranchValidator branchValidator, OrganizationValidator organizationValidator, PasswordEncoder passwordEncoder, UserOrganizationRepository userOrganizationRepository, UserOrganizationMapper userOrganizationMapper) {
        super(repository, mapper, validator);
        this.branchRepository = branchRepository;
        this.branchValidator = branchValidator;
        this.organizationValidator = organizationValidator;
        this.passwordEncoder = passwordEncoder;
        this.userOrganizationRepository = userOrganizationRepository;
        this.userOrganizationMapper = userOrganizationMapper;
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable, UserFilterDto filterDto) {
        String organizationId = validator.authenticateAndGetOrganizationId();

        Page<UserOrganization> memberships = userOrganizationRepository
                .findAllByOrganizationIdAndFilter(organizationId, filterDto.search(), pageable);

        return memberships.map(userOrganizationMapper::toUserDto);
    }

    @Override
    public UserDto get(String userId) {
        String organizationId = validator.authenticateAndGetOrganizationId();
        UserOrganization membership = userOrganizationRepository
                .findUserOrganizationByUserIdAndOrganizationId(userId, organizationId)
                .orElseThrow(() -> new RestException(ErrorType.USER_ORGANIZATION_NOT_FOUND, ErrorCodes.NotFound));

        return userOrganizationMapper.toUserDto(membership);
    }

    @Override
    public UserDto create(UserCreateDto createDto) {
        return null;
    }


    @Transactional
    public UserCreatedResponseDto createUser(UserCreateDto createDto) {
        String organizationId = validator.authenticateAndGetOrganizationId();

        Optional<User> existingUser = repository.findByPhoneAndDeletedFalse(createDto.phone());
        User targetUser;
        String generatedPassword = null;

        if (existingUser.isPresent()) {
            targetUser = existingUser.get();
            boolean alreadyHasRole = userOrganizationRepository
                    .checkIfAlreadyInOrganization(
                            targetUser.getId(),
                            organizationId,
                            createDto.role()
                    );

            if (alreadyHasRole) {
                throw new RestException(ErrorType.USER_ALREADY_EXISTS, ErrorCodes.AlreadyExists);
            }
        } else {
            validator.validate(createDto);
            targetUser = mapper.toEntity(createDto);
            generatedPassword = generatePassword(10);
            targetUser.setPassword(passwordEncoder.encode(generatedPassword));
            targetUser = repository.save(targetUser);
        }

        createUserOrganization(
                createDto.role(),
                createDto.permissions(),
                createDto.branchId(),
                targetUser,
                organizationId
        );

        return new UserCreatedResponseDto(
                targetUser.getId(),
                targetUser.getFullName(),
                targetUser.getPhone(),
                generatedPassword
        );
    }

    private void createUserOrganization(Role role, List<AdministratorPermission> permissions, String branchId, User save, String organizationId) {
        Organization organization = organizationValidator.validateAndGetId(organizationId);
        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setUser(save);
        userOrganization.setOrganization(organization);
        userOrganization.setActive(true);
        userOrganization.setRole(role);
        userOrganization.setPermissions(permissions);
        if (branchId != null && !branchId.isBlank()) {
            userOrganization.setBranch(branchValidator.validateIdAndGet(branchId));
        }
        userOrganizationRepository.save(userOrganization);
    }

    @Override
    public UserDto update(UserUpdateDto updateDto, String id) {
        User user = validator.authenticateAndGetUser();
        validator.validateIfCurrentUser(user, id);
        mapper.mapUpdate(user, updateDto);
        return mapper.toDto(repository.save(user));
    }

    @Override
    @Transactional
    public void delete(String id) {
        validator.validateId(id);
        softDeleteOrganizations(id);
    }

    public void softDeleteOrganizations(String id) {
        String organizationId = validator.authenticateAndGetOrganizationId();
        UserOrganization userOrganization = userOrganizationRepository.findUserOrganizationByUserIdAndOrganizationId(id, organizationId)
                .orElseThrow(() -> new RestException(ErrorType.USER_ORGANIZATION_NOT_FOUND, ErrorCodes.NotFound));
        userOrganization.setDeleted(true);
        userOrganizationRepository.save(userOrganization);
    }

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public UserDto createSuperAdmin(String organizationId, AdminUserCreateDto userCreateDto) {
        organizationValidator.validateId(organizationId);
        validator.validate(userCreateDto);
        User entity = mapper.toEntity(userCreateDto);
        entity.setPassword(passwordEncoder.encode(userCreateDto.password()));
        User save = repository.save(entity);
        createUserOrganization(Role.SUPER_ADMIN, null, userCreateDto.branchId(), save, organizationId);
        return new UserDto(
                save.getId(),
                userCreateDto.branchId(),
                null,
                save.getFullName(),
                save.getPhone(),
                null,
                Role.SUPER_ADMIN
        );
    }

    public void softDeleteUserAndOrganization(User user, String organizationId) {
        UserOrganization organization = userOrganizationRepository.findUserOrganizationByUserIdAndOrganizationId(user.getId(), organizationId).orElseThrow(() -> new RestException(ErrorType.USER_ORGANIZATION_MISMATCH, ErrorCodes.BadRequest));
        organization.setDeleted(true);
        userOrganizationRepository.save(organization);
    }
}
