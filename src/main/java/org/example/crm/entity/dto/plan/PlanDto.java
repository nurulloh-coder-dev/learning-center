package org.example.crm.entity.dto.plan;

import org.example.crm.entity.enums.FeatureKey;

import java.math.BigDecimal;
import java.util.Map;

public record PlanDto(
        String id,
        String code,
        String name,
        String description,
        BigDecimal price,
        String currency,
        Integer durationMonths,
        Boolean active,
        Integer sortOrder,
        Map<FeatureKey, Integer> limits
) {}