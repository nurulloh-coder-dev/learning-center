package org.example.crm.projection;

import org.example.crm.entity.model.Enrollment;

import java.math.BigDecimal;

public interface EnrollmentMonthlyFeeProjection {
    Enrollment getEnrollment();
    BigDecimal getMonthlyFee();
}
