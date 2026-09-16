package org.example.crm.entity.dto.plan;

import jakarta.validation.constraints.*;
import org.example.crm.entity.enums.FeatureKey;

import java.math.BigDecimal;
import java.util.Map;

public record PlanUpdateDto(
        @NotBlank @Size(max = 100) String name,
        String description,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal price,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull @Min(1) Integer durationMonths,
        Integer sortOrder,
        @NotNull Boolean active,
        @NotNull Map<FeatureKey, Integer> limits
) {}