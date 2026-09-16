package org.example.crm.entity.dto.teacher;

import org.example.crm.entity.dto.user.UserCreatedResponseDto;
import org.example.crm.entity.dto.user.UserDto;

public record TeacherCreateResponseDto(
        String id,
        UserCreatedResponseDto userDto,
        float totalTeachingExp,
        float currPlaceTeachingExp) {
}
