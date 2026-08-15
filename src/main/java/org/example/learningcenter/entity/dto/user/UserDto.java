package org.example.learningcenter.entity.dto.user;

import org.example.learningcenter.entity.enums.Role;

import java.time.LocalDate;

public record UserDto(String id,
                      String imageUrl,
                      String fullName,
                      String phone,
                      LocalDate birthDate,
                      Role role) {
}
