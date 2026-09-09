package org.example.crm.entity.dto.enrollment;

public record EnrollmentDto(
        String id,
        String studentId,
        String studentFullName,
        String groupId,
        String reason) {
}
