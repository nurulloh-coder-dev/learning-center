package org.example.crm.entity.dto.branch;

import java.math.BigDecimal;

public record BranchDto(
//        OrganizationDto organization,
        String id,
        BigDecimal chargeForMonth,
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
) {
}
