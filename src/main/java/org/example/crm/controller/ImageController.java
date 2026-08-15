package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.image.ImageDto;
import org.example.crm.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Page<ImageDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<ImageDto> images = imageService.getAll(pageable, search);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getById(@PathVariable String id) {
        ImageDto image = imageService.get(id);
        return ResponseEntity.ok(image);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> upload(@Valid @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(Map.of("imageUrl: ",imageService.uploadImage(file)));
    }

    @PutMapping("/main/{id}")
    public ResponseEntity<Void> update(
            @PathVariable String id
    ) {
        imageService.updateMainImg(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }


}