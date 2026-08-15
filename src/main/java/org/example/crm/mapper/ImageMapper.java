package org.example.crm.mapper;

import org.example.crm.entity.dto.image.ImageDto;
import org.example.crm.entity.model.Image;
import org.example.crm.projection.ImageProjection;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageDto toDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getImageUrl(),
                image.getOriginalFileName()
        );
    }

    public ImageDto toDto(ImageProjection imageProjection) {
        return new ImageDto(
                imageProjection.getId(),
                imageProjection.getImageUrl(),
                imageProjection.getOriginalName()
        );
    }
}
