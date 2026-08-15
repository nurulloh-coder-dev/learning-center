package org.example.learningcenter.entity.dto.branch;

import java.math.BigDecimal;

public record BranchCreateDto(
        BigDecimal chargeForMonth,
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
        ) {
}
