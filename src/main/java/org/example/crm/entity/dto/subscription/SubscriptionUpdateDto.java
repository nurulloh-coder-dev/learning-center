package org.example.crm.entity.dto.subscription;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.SubscriptionStatus;

// admin corrections only — status override, or a note edit. Not for renewals.
public record SubscriptionUpdateDto(
        @NotNull SubscriptionStatus status,
        String note
) {}