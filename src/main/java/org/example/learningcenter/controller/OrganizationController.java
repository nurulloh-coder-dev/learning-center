package org.example.learningcenter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.organization.OrganizationCreateDto;
import org.example.learningcenter.entity.dto.organization.OrganizationDto;
import org.example.learningcenter.entity.dto.organization.OrganizationUpdateDto;
import org.example.learningcenter.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<Page<OrganizationDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(organizationService.getAll(pageable, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(organizationService.get(id));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> create(@Valid @RequestBody OrganizationCreateDto createDto) {
        OrganizationDto created = organizationService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> update(
            @PathVariable String id,
            @Valid @RequestBody OrganizationUpdateDto updateDto) {
        return ResponseEntity.ok(organizationService.update(updateDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}