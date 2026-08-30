package org.example.crm.entity.analyticsRecord;

public record AnalyticEnrollment(
        Long enrollmentCount,
        Long enrollmentCountInMonth,
        Long enrollmentCountInPreviousMonth,
        Double differenceInPercentage
) {
}
