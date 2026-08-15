package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.config.CustomUserDetails;
import org.example.learningcenter.entity.dto.user.UserCreateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.User;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.UserRepository;
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
}
