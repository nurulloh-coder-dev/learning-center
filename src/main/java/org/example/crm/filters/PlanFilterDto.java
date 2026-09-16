package org.example.crm.filters;

public record PlanFilterDto(
        String search,
        Boolean active
) {}