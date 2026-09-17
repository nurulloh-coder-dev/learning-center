package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crm.config.CustomUserDetails;
import org.example.crm.entity.dto.subscription.SubscriptionCreateDto;
import org.example.crm.entity.dto.subscription.SubscriptionDto;
import org.example.crm.entity.dto.subscription.SubscriptionRenewDto;
import org.example.crm.entity.dto.subscription.SubscriptionUpdateDto;
import org.example.crm.entity.enums.SubscriptionStatus;
import org.example.crm.entity.model.Organization;
import org.example.crm.entity.model.Plan;
import org.example.crm.entity.model.Subscription;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.SubscriptionFilterDto;
import org.example.crm.mapper.SubscriptionMapper;
import org.example.crm.repository.SubscriptionRepository;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.PlanValidator;
import org.example.crm.validator.SubscriptionValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public SubscriptionDto create(SubscriptionCreateDto createDto) {
        Organization organization = organizationValidator.validateAndGetId(createDto.organizationId());
        Plan plan = planValidator.validateAndGetId(createDto.planId());
        Subscription subscription = repository.findCurrent(organization.getId())
                .orElseGet(Subscription::new);
        return activate(organization, plan, subscription, createDto.note());
    }

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
    }

    @Transactional
    public SubscriptionDto renewSubscription(String organizationId, SubscriptionRenewDto renewDto) {
        Subscription subscription = repository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new RestException(ErrorType.SUBSCRIPTION_NOT_FOUND, ErrorCodes.NotFound));
        Plan plan = renewDto.planId() != null ? planValidator.validateAndGetId(renewDto.planId()) : subscription.getPlan();
        return activate(subscription.getOrganization(), plan, subscription, renewDto.note());
    }


    private String currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        throw new RestException(ErrorType.UNAUTHORIZED, ErrorCodes.Forbidden);
    }

    private String currentUserOrganizationId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if (customUserDetails != null) {
                return customUserDetails.getOrganizationId();
            }
        }
        throw new RestException(ErrorType.UNAUTHORIZED, ErrorCodes.Forbidden);
    }

    private SubscriptionDto activate(Organization organization, Plan plan, Subscription subscription, String note) {
        LocalDate today = LocalDate.now();
        LocalDate base = subscription.getExpiryDate() != null && subscription.getExpiryDate().isAfter(today)
                ? subscription.getExpiryDate()
                : today;

        subscription.setOrganization(organization);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        if (subscription.getStartDate() == null) {
            subscription.setStartDate(today);
        }
        subscription.setExpiryDate(base.plusMonths(plan.getDurationMonths()));
        subscription.setPaidAmount(plan.getPrice());
        subscription.setCurrency(plan.getCurrency());
        subscription.setActivatedByUserId(currentUserId());
        subscription.setNote(note);

        return mapper.toDto(repository.save(subscription));
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Tashkent")
    public void syncSubscriptions() {
        List<Subscription> subscriptions = repository.findAllByForSync();
        List<Subscription> expiredSubscriptions = subscriptions.stream()
                .filter(s -> s.getExpiryDate().isBefore(LocalDate.now()))
                .toList();

        for (Subscription expiredSubscription : expiredSubscriptions) {
            switch (expiredSubscription.getStatus()) {
                case ACTIVE -> expiredSubscription.setStatus(SubscriptionStatus.GRACE);
                case GRACE -> {
                    if (expiredSubscription.getExpiryDate().plusDays(3).isBefore(LocalDate.now()))
                        expiredSubscription.setStatus(SubscriptionStatus.EXPIRED);
                }
            }
        }
        List<Subscription> changed = repository.saveAll(expiredSubscriptions);
        log.info("Subscription sync: checked {} subscriptions, updated {}", subscriptions.size(), changed.size());
    }


    public SubscriptionDto mySubscription() {
        String organizationId = currentUserOrganizationId();
        Subscription subscription = repository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new RestException(ErrorType.SUBSCRIPTION_NOT_FOUND, ErrorCodes.NotFound));
        return mapper.toDto(subscription);
    }
}