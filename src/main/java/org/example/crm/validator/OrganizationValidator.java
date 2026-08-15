package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.organization.OrganizationCreateDto;
import org.example.crm.entity.dto.organization.OrganizationUpdateDto;
import org.example.crm.entity.model.Organization;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.OrganizationRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrganizationValidator {
    private final OrganizationRepository repository;


    public Organization validateAndGetId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.ORGANIZATION_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate(OrganizationCreateDto createDto) {


    }

    public void validate(OrganizationUpdateDto updateDto) {


    }

    public void validateOrganizationMatch(String systemOrgId, String reqUserOrganizationId) {
        if (!Objects.equals(systemOrgId, reqUserOrganizationId)) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Unauthorized);
        }
    }
}
