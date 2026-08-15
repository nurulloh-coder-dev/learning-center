package org.example.crm.entity.dto.enrollment;

import jakarta.annotation.Nonnull;

public record EnrollmentCreateDto(String studentId, @Nonnull String groupId, String reason) {
}
