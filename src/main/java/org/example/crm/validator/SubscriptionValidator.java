package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.subscription.SubscriptionCreateDto;
import org.example.crm.entity.model.Subscription;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionValidator {

    private final SubscriptionRepository repository;

    public void validate(SubscriptionCreateDto createDto) {
        // organizationId / planId existence is checked in the service,
        // since it needs the actual entities anyway — no point loading twice
    }

    public Subscription validateAndGetId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.SUBSCRIPTION_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateOrganizationMatch(String subscriptionId, String organizationId) {
        Subscription s = validateAndGetId(subscriptionId);
        if (!s.getOrganization().getId().equals(organizationId)) {
            throw new RestException(ErrorType.ORGANIZATION_ID_MISMATCH,ErrorCodes.BadRequest);
        }
    }
}