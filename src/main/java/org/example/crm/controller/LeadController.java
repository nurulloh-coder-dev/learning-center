package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.service.LeadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<Page<LeadDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LeadStatus status) {
        return ResponseEntity.ok(leadService.getAll(pageable, search, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(leadService.get(id));
    }

    @PostMapping
    public ResponseEntity<LeadDto> create(@Valid @RequestBody LeadCreateDto createDto) {
        LeadDto createdLead = leadService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadDto> update(
            @PathVariable String id,
            @Valid @RequestBody LeadUpdateDto updateDto) {
        return ResponseEntity.ok(leadService.update(updateDto, id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadDto> updateStatus(
            @PathVariable String id,
            @RequestParam LeadStatus status) {
        return ResponseEntity.ok(leadService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        leadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
