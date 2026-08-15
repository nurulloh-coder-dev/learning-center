package org.example.crm.entity.dto.user;

import org.example.crm.entity.enums.Role;

import java.time.LocalDate;

public record UserCreateDto(String fullName, String phone, LocalDate birthDate, Role role,String branchId) {
}
