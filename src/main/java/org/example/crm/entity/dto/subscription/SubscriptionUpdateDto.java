package org.example.crm.entity.dto.subscription;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.SubscriptionStatus;

public record SubscriptionUpdateDto(
        @NotNull SubscriptionStatus status,
        String note
) {}