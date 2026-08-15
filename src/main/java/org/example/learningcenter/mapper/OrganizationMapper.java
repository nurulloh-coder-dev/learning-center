package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.annotation.IgnoreAuditFields;
import org.example.learningcenter.entity.dto.organization.OrganizationCreateDto;
import org.example.learningcenter.entity.dto.organization.OrganizationDto;
import org.example.learningcenter.entity.dto.organization.OrganizationUpdateDto;
import org.example.learningcenter.entity.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface OrganizationMapper {

    OrganizationDto toDto(Organization organization);

    @IgnoreAuditFields
    Organization toEntity(OrganizationCreateDto createDto);

    void mapUpdate(@MappingTarget Organization organization, OrganizationUpdateDto organizationUpdateDto);
}
