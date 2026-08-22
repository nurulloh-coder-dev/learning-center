package org.example.crm.mapper;

import org.example.crm.annotation.IgnoreAuditFields;
import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.model.Lead;
import org.example.crm.projection.LeadProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "callAt", source = "callAt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "preferredCourse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LeadDto toDto(LeadProjection leadProjection);

    @IgnoreAuditFields
    @Mapping(target = "status", ignore = true)
    Lead toEntity(LeadCreateDto createDto);

    LeadDto toDto(Lead lead);

    @IgnoreAuditFields
    void mapUpdate(@MappingTarget Lead lead, LeadUpdateDto updateDto);
}
