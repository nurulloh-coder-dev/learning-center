package org.example.crm.entity.dto.user;

import org.example.crm.entity.enums.Role;

import java.time.LocalDate;

public record UserDto(String id,
                      String branchId,
                      String imageUrl,
                      String fullName,
                      String phone,
                      LocalDate birthDate,
                      Role role) {
}
