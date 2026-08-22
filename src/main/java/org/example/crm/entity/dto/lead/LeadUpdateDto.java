package org.example.crm.entity.dto.lead;

import jakarta.validation.constraints.NotNull;
import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;

import java.time.LocalDateTime;

public record LeadUpdateDto(
        String fullName,
        String phone,

        @NotNull(message = "Lead status cannot be null")
        LeadStatus status,

        LeadSource source,
        String preferredCourseName,
        LocalDateTime callAt
) {
}
