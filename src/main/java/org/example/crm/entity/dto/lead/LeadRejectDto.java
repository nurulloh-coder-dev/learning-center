package org.example.crm.entity.dto.lead;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.RejectionReason;

public record LeadRejectDto(
        @NotNull(message = "Rejection reason is required")
        RejectionReason reason,
        String note
) {}