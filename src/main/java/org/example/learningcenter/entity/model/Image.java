package org.example.learningcenter.entity.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.learningcenter.entity.base.BaseEntity;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image extends BaseEntity {

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String s3Key;

    private String originalFileName;
    private String contentType;
    private Long fileSize;

}