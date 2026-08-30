package org.example.crm.entity.analyticsRecord;

public record AnalyticStudent(
        Integer studentCount,
        Integer studentsAddedInPrevMonth,
        Integer studentsAddedInMonth,
        double difference
) {
}
