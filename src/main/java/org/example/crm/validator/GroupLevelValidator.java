package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.config.CustomUserDetails;
import org.example.crm.entity.dto.groupLevel.GroupLevelCreateDto;
import org.example.crm.entity.model.Level;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.GroupLevelRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupLevelValidator {
    final GroupLevelRepository groupRepository;

    public Level validateAndGet(String id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.GROUP_LEVEL_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateForCreate(GroupLevelCreateDto createDto) {
        String organizationId = authenticateAndGetOrganizationId();

        Integer maxOrderNumber = groupRepository.getMaxOrderNumberByOrganizationId(organizationId);
        if (createDto.orderNumber() == null
                || (maxOrderNumber != null && createDto.orderNumber() <= maxOrderNumber)) {
            throw new RestException(ErrorType.GROUP_LEVEL_ORDER_ALREADY_EXISTS, ErrorCodes.BadRequest);
        }

        if (groupRepository.existsByOrganizationIdAndNameIgnoreCase(organizationId, createDto.name())) {
            throw new RestException(ErrorType.GROUP_LEVEL_NAME_ALREADY_EXISTS, ErrorCodes.AlreadyExists);
        }

        Integer lessonCount = createDto.lessonCount();
        Integer durationInMonths = createDto.durationInMonths();
        if (lessonCount == null || lessonCount <= 0 || durationInMonths == null || durationInMonths <= 0
                || lessonCount % durationInMonths != 0) {
            throw new RestException(ErrorType.INCORRECT_GROUP_LEVEL_COUNT, ErrorCodes.BadRequest);
        }
    }

    private String authenticateAndGetOrganizationId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails principal) {
            return principal.getOrganizationId();
        }
        throw new RestException(ErrorType.UNAUTHORIZED, ErrorCodes.Unauthorized);
    }
}
