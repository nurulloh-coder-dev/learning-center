package org.example.learningcenter.entity.dto.student;

import org.example.learningcenter.entity.dto.user.UserUpdateDto;

public record StudentUpdateDto(UserUpdateDto user, String parentPhone) {
}
