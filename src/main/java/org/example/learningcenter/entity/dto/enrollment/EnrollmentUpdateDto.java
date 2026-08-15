package org.example.learningcenter.entity.dto.enrollment;

import jakarta.annotation.Nonnull;

public record EnrollmentUpdateDto(@Nonnull String studentId, @Nonnull String groupId) {
}
