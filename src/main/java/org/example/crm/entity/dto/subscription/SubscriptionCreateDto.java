package org.example.crm.entity.dto.subscription;

import jakarta.validation.constraints.NotBlank;

// this is the "create" — admin picks an org and a plan, everything else is computed
public record SubscriptionCreateDto(
        @NotBlank String organizationId,
        @NotBlank String planId,
        String note
) {}