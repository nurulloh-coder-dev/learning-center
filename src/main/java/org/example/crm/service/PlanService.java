package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.plan.PlanCreateDto;
import org.example.crm.entity.dto.plan.PlanDto;
import org.example.crm.entity.dto.plan.PlanUpdateDto;
import org.example.crm.entity.model.Plan;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.PlanFilterDto;
import org.example.crm.mapper.PlanMapper;
import org.example.crm.repository.PlanRepository;
import org.example.crm.validator.PlanValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlanService extends AbstractService<
        PlanRepository,
        PlanMapper,
        PlanValidator> implements CrudService<PlanFilterDto, PlanCreateDto, PlanUpdateDto, PlanDto, String, Page<PlanDto>> {

    protected PlanService(PlanRepository repository, PlanMapper mapper, PlanValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public Page<PlanDto> getAll(Pageable pageable, PlanFilterDto filterDto) {
        return repository.findAll(filterDto.search(), pageable).map(mapper::toDto);
    }

    @Override
    public PlanDto get(String id) {
        return mapper.toDto(validator.validateAndGetId(id));
    }

    @Override
    public PlanDto create(PlanCreateDto createDto) {
        validator.validate(createDto);
        Plan entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PlanDto update(PlanUpdateDto updateDto, String id) {
        validator.validate(updateDto);
        Plan plan = validator.validateAndGetId(id);
        mapper.mapUpdate(plan, updateDto);
        return mapper.toDto(repository.save(plan));
    }

    @Override
    public void delete(String id) {
        Plan plan = validator.validateAndGetId(id);
        // real block, not a courtesy check — deleting a plan under a live
        // subscription orphans the FK and breaks every limit lookup for that org
        if (repository.hasActiveSubscriptions(id)) {
            throw new RestException(ErrorType.PLAN_IN_USE, ErrorCodes.BadRequest);
        }
        repository.softDelete(id);
    }
}