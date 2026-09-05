package org.example.crm.entity.dto.user;

import java.time.LocalDate;

public record UserUpdateDto(String fullName, String phone, LocalDate birthDate, String imageUrl) {
}
