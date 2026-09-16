package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.plan.PlanCreateDto;
import org.example.crm.entity.dto.plan.PlanDto;
import org.example.crm.entity.dto.plan.PlanUpdateDto;
import org.example.crm.filters.PlanFilterDto;
import org.example.crm.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService service;

    @GetMapping
    public Page<PlanDto> getAll(Pageable pageable, PlanFilterDto filterDto) {
        return service.getAll(pageable, filterDto);
    }

    @GetMapping("/{id}")
    public PlanDto get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanDto create(@Valid @RequestBody PlanCreateDto createDto) {
        return service.create(createDto);
    }

    @PutMapping("/{id}")
    public PlanDto update(@PathVariable String id, @Valid @RequestBody PlanUpdateDto updateDto) {
        return service.update(updateDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}