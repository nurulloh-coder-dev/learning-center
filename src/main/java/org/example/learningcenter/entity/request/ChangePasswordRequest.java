package org.example.learningcenter.entity.request;

public record ChangePasswordRequest(
        String newPassword,
        String oldPassword,
        String confirmPassword
) {
}
