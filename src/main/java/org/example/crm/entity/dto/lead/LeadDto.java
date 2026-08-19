package org.example.crm.entity.dto.lead;

import org.example.crm.entity.enums.GroupLevel;
import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;

import java.time.LocalDateTime;

public record LeadDto(
        String id,
        String fullName,
        String phone,
        LocalDateTime callAt,
        LeadStatus status,
        LeadSource source,
        GroupLevel preferredCourse,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
