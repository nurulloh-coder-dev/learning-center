package org.example.crm.entity.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseDto(@NotBlank String name) {
}
