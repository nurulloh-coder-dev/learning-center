package org.example.learningcenter.service;

import jakarta.validation.Valid;
import org.example.learningcenter.entity.dto.image.ImageCreateDto;
import org.example.learningcenter.entity.dto.image.ImageDto;
import org.example.learningcenter.entity.dto.image.ImageUpdateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Image;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.mapper.ImageMapper;
import org.example.learningcenter.projection.ImageProjection;
import org.example.learningcenter.repository.ImageRepository;
import org.example.learningcenter.validator.ImageValidator;
import org.example.learningcenter.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService extends AbstractService<
        ImageRepository,
        ImageMapper,
        ImageValidator> implements CrudService<ImageCreateDto, ImageUpdateDto, ImageDto,String>{

    private final S3Service s3Service;
    private final UserValidator userValidator;

    protected ImageService(ImageRepository repository, ImageMapper mapper, ImageValidator validator, S3Service s3Service, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.s3Service = s3Service;
        this.userValidator = userValidator;
    }

    @Override
    public Page<ImageDto> getAll(Pageable pageable, String search) {
        String userId = userValidator.authenticateAndGetId();
        Page<ImageProjection> allByUserId = repository.findAllByUserId(userId,pageable);
        return allByUserId.map(mapper::toDto);
    }

    @Override
    public ImageDto get(String id) {
        return null;
    }

    @Override
    public ImageDto create(ImageCreateDto createDto) {
        return null;
    }

    @Override
    public ImageDto update(ImageUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public ImageDto uploadImage(@Valid MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        boolean validExtension = filename != null && filename.toLowerCase().endsWith(".pdf");
        boolean validContentType = "application/pdf".equals(file.getContentType());

        if (!validExtension || !validContentType) {
            throw new RestException(ErrorType.INVALID_FILE_TYPE, ErrorCodes.BadRequest);
        }
        Image image = new Image();
        image.setOriginalFileName(filename);
        image.setFileSize(file.getSize());
        image.setContentType(file.getContentType());
        String key = s3Service.uploadFile(file);
        image.setS3Key(key);
        String presignedUrl = s3Service.getPublicUrl(key);
        image.setImageUrl(presignedUrl);
        Image save = repository.save(image);
        return mapper.toDto(save);
    }

    public void updateMainImg(String id) {
        validator.validateId(id);
        String userId = userValidator.authenticateAndGetId();
        repository.updateMainImg(id,userId);
    }
}
