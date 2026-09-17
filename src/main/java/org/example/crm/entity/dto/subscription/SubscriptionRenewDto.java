package org.example.crm.entity.dto.subscription;

public record SubscriptionRenewDto(
        String planId,       // nullable — null means "keep current plan"
        String note
) {}