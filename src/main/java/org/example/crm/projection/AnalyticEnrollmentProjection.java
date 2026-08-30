package org.example.crm.projection;

public interface AnalyticEnrollmentProjection {
    Long getEnrollmentCount();
    Long getEnrollmentCountInMonth();
    Long getEnrollmentCountInPreviousMonth();
}
