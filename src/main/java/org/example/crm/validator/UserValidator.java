package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.config.CustomUserDetails;
import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.enums.Role;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository repository;


    public User validateIdAndGet(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.USER_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate(UserCreateDto createDto) {

    }

    public User authenticateAndGetUser(){
        String id = authenticateAndGetId();
        return repository.findById(id).orElseThrow(() -> new RestException(ErrorType.USER_NOT_FOUND, ErrorCodes.NotFound));
    }

    public String authenticateAndGetId() {
        return getPrincipal().getUserId();
    }

    public String authenticateAndGetOrganizationId() {
        String orgId = getPrincipal().getOrganizationId();
        if (orgId == null) {
            throw new RestException(ErrorType.ORGANIZATION_NOT_FOUND, ErrorCodes.NotFound);
        }
        return orgId;
    }

    private CustomUserDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails principal) {
            return principal;
        }

        throw new RestException(ErrorType.UNAUTHORIZED, ErrorCodes.Unauthorized);
    }

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.USER_NOT_FOUND,ErrorCodes.NotFound);
        }
    }

    public void validateUserPermission(User entity) {
        if (entity.getPermissions() != null && !entity.getRole().equals(Role.ADMINISTRATOR)){
            throw new RestException(ErrorType.PERMISSION_ONLY_FOR_ADMINISTRATOR, ErrorCodes.BadRequest);
        }
    }

    public void validateAdministratorPermission(User entity, AdministratorPermission permission) {
        if (entity.getRole().equals(Role.ADMINISTRATOR)){
            entity.getPermissions().stream()
                    .filter(p -> p.equals(permission))
                    .findFirst().orElseThrow(() -> new RestException(ErrorType.NO_PERMISSION,  ErrorCodes.BadRequest));
        }
    }

    public void validateIfCurrentUser(User user, String id) {
        if (!user.getId().equals(id)) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Forbidden);
        }
    }
}
