package org.example.crm.entity.analyticsRecord;

public record AnalyticStudent(
        Long studentCount,
        Long studentsAddedInPrevMonth,
        Long studentsAddedInMonth,
        double difference
) {
}
