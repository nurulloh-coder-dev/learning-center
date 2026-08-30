package org.example.crm.entity.analyticsRecord;

public record AnalyticTeacher(
        Integer teacherCount,
        Integer teachersAddedInMonth,
        Integer teachersAddedInPrevMonth,
        double difference
) {
}
