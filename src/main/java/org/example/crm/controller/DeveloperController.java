package org.example.crm.controller;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.user.AdminUserCreateDto;
import org.example.crm.entity.dto.user.UserCreateDto;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('DEVELOPER')")
@RequestMapping("/api/v1/developer")
public class DeveloperController {
    final UserService userService;

    @PostMapping("/create-super-admin")
    public ResponseEntity<UserDto> createSuperAdmin(@RequestParam String organizationId, @RequestBody AdminUserCreateDto userCreateDto) {
        return ResponseEntity.status(201).body(userService.createSuperAdmin(organizationId, userCreateDto));
    }
}
