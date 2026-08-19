package org.example.crm.entity.dto.lead;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.crm.entity.enums.GroupLevel;
import org.example.crm.entity.enums.LeadSource;

public record LeadCreateDto(
        @NotBlank(message = "fullName is required")
        String fullName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
        String phone,

        LeadSource source,
        GroupLevel preferredCourse
) {}
