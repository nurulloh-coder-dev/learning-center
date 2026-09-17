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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService service;

    @GetMapping
    public ResponseEntity<Page<PlanDto>> getAll(Pageable pageable, PlanFilterDto filterDto) {
        return ResponseEntity.ok(service.getAll(pageable, filterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanDto> get(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }


    @PostMapping
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<PlanDto> create(@Valid @RequestBody PlanCreateDto createDto) {
        PlanDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<PlanDto> update(@PathVariable String id, @Valid @RequestBody PlanUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}