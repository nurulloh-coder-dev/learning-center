package org.example.crm.projection;

public interface AnalyticEnrollmentProjection {
    Integer getEnrollmentCount();
    Integer getEnrollmentCountInMonth();
    Integer getEnrollmentCountInPreviousMonth();
}
