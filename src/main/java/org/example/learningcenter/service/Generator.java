package org.example.learningcenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class Generator {
    private final Random random = new Random();
    private final PasswordEncoder passwordEncoder;

    public String generatePassword(LocalDateTime dateOfBirth) {
        return passwordEncoder.encode(dateOfBirth.toString().replace('-','.'));
    }

    public String generateSuperAdminName(String orgName) {
        StringBuilder sb = new StringBuilder(orgName);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = alphabet.length();
        for (int i = 0; i < 5; i++) {
            sb.append(alphabet.charAt(random.nextInt(0, length)));
        }
        return sb.toString();
    }
}
