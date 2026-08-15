package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.organization.OrganizationCreateDto;
import org.example.learningcenter.entity.dto.organization.OrganizationUpdateDto;
import org.example.learningcenter.entity.model.Organization;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.OrganizationRepository;
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
