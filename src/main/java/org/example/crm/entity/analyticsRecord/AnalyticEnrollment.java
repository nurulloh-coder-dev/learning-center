package org.example.crm.entity.analyticsRecord;

public record AnalyticEnrollment(
        Integer enrollmentCount,
        Integer enrollmentCountInMonth,
        Integer enrollmentCountInPreviousMonth,
        Double differenceInPercentage
) {
}
