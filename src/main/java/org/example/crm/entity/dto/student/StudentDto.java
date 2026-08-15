package org.example.crm.entity.dto.student;

import org.example.crm.entity.dto.user.UserDto;

public record StudentDto(String id,
                         UserDto userDto,
                         String parentPhone) {
}
