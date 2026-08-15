package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.dto.image.ImageDto;
import org.example.learningcenter.entity.model.Image;
import org.example.learningcenter.projection.ImageProjection;
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
