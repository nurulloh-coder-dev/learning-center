package org.example.learningcenter.entity.dto.teacher;

import org.example.learningcenter.entity.dto.user.UserDto;

public record TeacherDto(
        String id,
        UserDto userDto
) {
}
