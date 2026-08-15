package org.example.crm.entity.dto.teacher;

import org.example.crm.entity.dto.user.UserDto;

public record TeacherDto(
        String id,
        UserDto userDto
) {
}
