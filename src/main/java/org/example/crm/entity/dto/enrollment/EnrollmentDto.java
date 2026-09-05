package org.example.crm.entity.dto.enrollment;

import org.example.crm.entity.enums.EnrollmentPaymentStatus;

import java.math.BigDecimal;

public record EnrollmentDto(
        String id,
        String studentId,
        String groupId,
        String reason,
        BigDecimal monthlyFee,
        BigDecimal paidAmount,
        EnrollmentPaymentStatus status) {
}
