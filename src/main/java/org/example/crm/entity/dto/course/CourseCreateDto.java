package org.example.crm.entity.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseCreateDto(@NotBlank String name) {
}
