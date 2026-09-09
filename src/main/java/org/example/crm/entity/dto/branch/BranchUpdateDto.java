package org.example.crm.entity.dto.branch;

public record BranchUpdateDto(
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
) {
}
