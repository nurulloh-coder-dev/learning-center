package org.example.learningcenter.service;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.config.AwsConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final AwsConfig config;


    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(config.getBucketName())
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return key;
    }

    public String getPublicUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", config.getBucketName(), config.getRegion(), key);
    }

    //// https://learning-center-uploads.s3.eu-north-1.amazonaws.com/https://learning-center-uploads.s3.eu-north-1.amazonaws.com/d544f322-ecc1-45f1-a7b4-54194d2de90d-java-middle-interview-prep.pdf
}
