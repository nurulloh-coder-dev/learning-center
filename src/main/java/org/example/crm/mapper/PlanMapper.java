package org.example.crm.mapper;

import org.example.crm.entity.dto.plan.PlanCreateDto;
import org.example.crm.entity.dto.plan.PlanDto;
import org.example.crm.entity.dto.plan.PlanUpdateDto;
import org.example.crm.entity.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    Plan toEntity(PlanCreateDto createDto);

    PlanDto toDto(Plan plan);

    void mapUpdate(@MappingTarget Plan plan, PlanUpdateDto updateDto);
}