package org.example.learningcenter.entity.dto.user;

import org.example.learningcenter.entity.enums.Role;

import java.time.LocalDate;

public record UserCreateDto(String fullName, String phone, LocalDate birthDate, Role role,String branchId) {
}
