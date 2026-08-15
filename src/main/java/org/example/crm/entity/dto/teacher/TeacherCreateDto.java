package org.example.crm.entity.dto.teacher;

import org.example.crm.entity.dto.user.UserCreateDto;

public record TeacherCreateDto(UserCreateDto user) {
}
