package org.example.crm.entity.dto.subscription;

import org.example.crm.entity.dto.IdNameDto;
import org.example.crm.entity.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record SubscriptionDto(
        String id,
        IdNameDto organization,
        IdNameDto plan,
        SubscriptionStatus status,
        Instant startsAt,
        Instant expiresAt,
        BigDecimal paidAmount,
        String currency,
        String activatedByUserId,
        String note
) {}