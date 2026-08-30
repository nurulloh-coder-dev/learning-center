package org.example.crm.entity.analyticsRecord;

public record AnalyticTeacher(
        Long teacherCount,
        Long teachersAddedInMonth,
        Long teachersAddedInPrevMonth,
        double difference
) {
}
