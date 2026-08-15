package org.example.crm.entity.dto.student;

import org.example.crm.entity.dto.user.UserCreateDto;

public record StudentCreateDto(UserCreateDto userCreateDto,
                               String parentPhone) {

}
