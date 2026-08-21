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

    LeadDto toDto(LeadProjection leadProjection);

    @IgnoreAuditFields
    @Mapping(target = "status", ignore = true)
    Lead toEntity(LeadCreateDto createDto);

    LeadDto toDto(Lead lead);

    @IgnoreAuditFields
    void mapUpdate(@MappingTarget Lead lead, LeadUpdateDto updateDto);
}
