package org.example.crm.entity.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseUpdateDto(@NotBlank String name) {
}
