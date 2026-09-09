package org.example.crm.entity.dto.branch;


public record BranchCreateDto(
        String name,
        String address,
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String googleMapsUrl
        ) {
}
