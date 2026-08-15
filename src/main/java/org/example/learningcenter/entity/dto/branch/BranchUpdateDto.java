package org.example.learningcenter.entity.dto.branch;

import java.math.BigDecimal;

public record BranchUpdateDto(
        BigDecimal chargeForMonth,
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
) {
}
