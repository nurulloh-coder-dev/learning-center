package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.subscription.SubscriptionCreateDto;
import org.example.crm.entity.dto.subscription.SubscriptionDto;
import org.example.crm.entity.dto.subscription.SubscriptionUpdateDto;
import org.example.crm.entity.enums.SubscriptionStatus;
import org.example.crm.entity.model.Organization;
import org.example.crm.entity.model.Plan;
import org.example.crm.entity.model.Subscription;
import org.example.crm.filters.SubscriptionFilterDto;
import org.example.crm.mapper.SubscriptionMapper;
import org.example.crm.repository.SubscriptionRepository;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.PlanValidator;
import org.example.crm.validator.SubscriptionValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final SubscriptionMapper mapper;
    private final SubscriptionValidator validator;
    private final OrganizationValidator organizationValidator;
    private final PlanValidator planValidator;

    public Page<SubscriptionDto> getAll(Pageable pageable, SubscriptionFilterDto filterDto) {
        return repository.findAll(filterDto.search(), filterDto.status(), pageable).map(mapper::toDto);
    }

    public SubscriptionDto get(String id) {
        return mapper.toDto(validator.validateAndGetId(id));
    }

    // this is the "activate/renew" action — extends from current expiry if still active,
    // otherwise starts fresh from now. One method covers both first purchase and renewal.
    @Transactional
    public SubscriptionDto create(SubscriptionCreateDto createDto) {
        validator.validate(createDto);
        Organization organization = organizationValidator.validateAndGetId(createDto.organizationId());
        Plan plan = planValidator.validateAndGetId(createDto.planId());

        Instant base = repository.findCurrent(organization.getId())
                .map(Subscription::getExpiresAt)
                .filter(end -> end.isAfter(Instant.now()))
                .orElse(Instant.now());

        Subscription subscription = repository.findCurrent(organization.getId()).orElseGet(Subscription::new);
        subscription.setOrganization(organization);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        if (subscription.getStartsAt() == null) {
            subscription.setStartsAt(Instant.now());
        }
        subscription.setExpiresAt(base.plus(Period.ofMonths(plan.getDurationMonths())));
        subscription.setPaidAmount(plan.getPrice());
        subscription.setCurrency(plan.getCurrency());
        subscription.setActivatedByUserId(currentUserId());
        subscription.setNote(createDto.note());

        return mapper.toDto(repository.save(subscription));
    }

    // admin correction only: force a status (e.g. manual CANCELED), not for renewals
    @Transactional
    public SubscriptionDto update(SubscriptionUpdateDto updateDto, String id) {
        Subscription subscription = validator.validateAndGetId(id);
        subscription.setStatus(updateDto.status());
        subscription.setNote(updateDto.note());
        return mapper.toDto(repository.save(subscription));
    }

    @Transactional
    public void delete(String id) {
        Subscription subscription = validator.validateAndGetId(id);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        repository.save(subscription);
        // soft-cancel, not repository.softDelete — you want CANCELED subscriptions
        // to still show up in an org's history/reporting, not disappear
    }

    private String currentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}