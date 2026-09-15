package org.example.crm.filters;

import org.example.crm.entity.enums.SubscriptionStatus;

public record SubscriptionFilterDto(
        String search,          // matches organization name
        SubscriptionStatus status
) {}