package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.plan.PlanCreateDto;
import org.example.crm.entity.dto.plan.PlanUpdateDto;
import org.example.crm.entity.model.Plan;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.PlanRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanValidator {

    private final PlanRepository repository;

    public void validate(PlanCreateDto createDto) {
        if (repository.existsByCode(createDto.code())) {
            throw new RestException(ErrorType.PLAN_ALREADY_EXISTS, ErrorCodes.AlreadyExists);
        }
    }

    public void validate(PlanUpdateDto updateDto) {
        // no cross-field rules yet; kept for symmetry with other services
    }

    public Plan validateAndGetId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.PLAN_NOT_FOUND,ErrorCodes.NotFound));
    }
}