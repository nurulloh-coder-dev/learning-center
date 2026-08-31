package org.example.crm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadDto;
import org.example.crm.entity.dto.lead.LeadRejectDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.service.LeadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMINISTRATOR') and hasAuthority('LEAD_MANAGEMENT'))")
public class LeadController {

    private final LeadService service;

    @GetMapping
    public ResponseEntity<Page<LeadDto>> getAll(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LeadStatus status) {
        return ResponseEntity.ok(service.getAll(pageable, search, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<LeadDto> create(@Valid @RequestBody LeadCreateDto createDto) {
        LeadDto createdLead = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<LeadDto> enroll(
            @PathVariable String id,
            @RequestParam String groupId) {
        LeadDto enroll = service.enroll(id, groupId);
        return ResponseEntity.ok(enroll);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadDto> update(
            @PathVariable String id,
            @Valid @RequestBody LeadUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto, id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LeadDto> rejectLead(
            @PathVariable String id,
            @Valid @RequestBody LeadRejectDto dto) {
        return ResponseEntity.ok(service.reject(id, dto));
    }

    @PatchMapping("/{id}/callLater")
    public ResponseEntity<LeadDto> updateStatus(
            @PathVariable String id,
            @RequestParam @Future @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime callAt) {
        return ResponseEntity.ok(service.callLater(id, callAt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
