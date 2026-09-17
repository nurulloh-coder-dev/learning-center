package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.subscription.SubscriptionCreateDto;
import org.example.crm.entity.dto.subscription.SubscriptionDto;
import org.example.crm.entity.dto.subscription.SubscriptionRenewDto;
import org.example.crm.entity.dto.subscription.SubscriptionUpdateDto;
import org.example.crm.filters.SubscriptionFilterDto;
import org.example.crm.service.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping
    public ResponseEntity<Page<SubscriptionDto>> getAll(Pageable pageable, SubscriptionFilterDto filterDto) {
        return ResponseEntity.ok(service.getAll(pageable, filterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> get(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SUPER_ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<SubscriptionDto> mySubscription(){
        SubscriptionDto subscriptionDto = service.mySubscription();
        return ResponseEntity.ok(subscriptionDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<SubscriptionDto> create(@Valid @RequestBody SubscriptionCreateDto createDto) {
        SubscriptionDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/renew/{orgId}")
    public ResponseEntity<SubscriptionDto> renewSubscription(@PathVariable String orgId, @RequestBody SubscriptionRenewDto dto) {
        SubscriptionDto subscriptionDto = service.renewSubscription(orgId, dto);
        return ResponseEntity.ok(subscriptionDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<SubscriptionDto> update(@PathVariable String id, @Valid @RequestBody SubscriptionUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}