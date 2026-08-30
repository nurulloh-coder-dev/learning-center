package org.example.crm.entity.analyticsRecord;

public record AnalyticLead(
        Long leadCount,
        Long leadCountPreviousMonth,
        Long leadCountInAMonth,
        double difference) {
}
