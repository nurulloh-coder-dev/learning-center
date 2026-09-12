package org.example.crm.entity.dto.user;


public record AdminUserCreateDto(
        String fullName,
        String phone,
        String password,
        String branchId
) {
}
