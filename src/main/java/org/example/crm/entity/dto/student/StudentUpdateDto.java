package org.example.crm.entity.dto.student;

import org.example.crm.entity.dto.user.UserUpdateDto;

public record StudentUpdateDto(UserUpdateDto user, String parentPhone) {
}
