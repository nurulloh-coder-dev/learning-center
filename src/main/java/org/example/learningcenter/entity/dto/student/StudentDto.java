package org.example.learningcenter.entity.dto.student;

import org.example.learningcenter.entity.dto.user.UserDto;

public record StudentDto(String id,
                         UserDto userDto,
                         String parentPhone) {
}
