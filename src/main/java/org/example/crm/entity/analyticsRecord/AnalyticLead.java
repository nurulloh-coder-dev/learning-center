package org.example.crm.entity.analyticsRecord;

public record AnalyticLead(
        Integer leadCount,
        Integer leadCountPreviousMonth,
        Integer leadCountInAMonth,
        double difference) {
}
