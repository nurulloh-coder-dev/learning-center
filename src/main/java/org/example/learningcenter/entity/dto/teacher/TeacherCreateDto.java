package org.example.learningcenter.entity.dto.teacher;

import org.example.learningcenter.entity.dto.user.UserCreateDto;

public record TeacherCreateDto(UserCreateDto user) {
}
