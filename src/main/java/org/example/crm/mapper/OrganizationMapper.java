package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.organization.OrganizationCreateDto;
import org.example.crm.entity.dto.organization.OrganizationDto;
import org.example.crm.entity.dto.organization.OrganizationUpdateDto;
import org.example.crm.entity.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface OrganizationMapper {

    OrganizationDto toDto(Organization organization);

    @IgnoreAuditFields
    Organization toEntity(OrganizationCreateDto createDto);

    void mapUpdate(@MappingTarget Organization organization, OrganizationUpdateDto organizationUpdateDto);
}
