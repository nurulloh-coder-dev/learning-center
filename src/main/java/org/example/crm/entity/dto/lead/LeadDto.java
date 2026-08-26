package org.example.crm.entity.dto.lead;

import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.entity.model.Level;

import java.time.LocalDateTime;

public record LeadDto(
        String id,
        String fullName,
        String phone,
        LocalDateTime callAt,
        LeadStatus status,
        LeadSource source,
        String preferredCourse
) {
}
