package org.example.crm.entity.dto.branch;

public record BranchDto(
        String id,
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
) {
}
