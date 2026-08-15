package org.example.learningcenter.entity.dto.student;

import org.example.learningcenter.entity.dto.user.UserCreateDto;

public record StudentCreateDto(UserCreateDto userCreateDto,
                               String parentPhone) {

}
