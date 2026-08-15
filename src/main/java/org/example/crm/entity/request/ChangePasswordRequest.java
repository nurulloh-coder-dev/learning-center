package org.example.crm.entity.request;

public record ChangePasswordRequest(
        String newPassword,
        String oldPassword,
        String confirmPassword
) {
}
