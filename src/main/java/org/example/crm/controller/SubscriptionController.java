package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.subscription.SubscriptionCreateDto;
import org.example.crm.entity.dto.subscription.SubscriptionDto;
import org.example.crm.entity.dto.subscription.SubscriptionUpdateDto;
import org.example.crm.filters.SubscriptionFilterDto;
import org.example.crm.service.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping
    public Page<SubscriptionDto> getAll(Pageable pageable, SubscriptionFilterDto filterDto) {
        return service.getAll(pageable, filterDto);
    }

    @GetMapping("/{id}")
    public SubscriptionDto get(@PathVariable String id) {
        return service.get(id);
    }

    // admin activates/renews a subscription after confirming a manual payment
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto create(@Valid @RequestBody SubscriptionCreateDto createDto) {
        return service.create(createDto);
    }

    @PutMapping("/{id}")
    public SubscriptionDto update(@PathVariable String id, @Valid @RequestBody SubscriptionUpdateDto updateDto) {
        return service.update(updateDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}