package org.example.learningcenter.controller;

import jakarta.validation.Valid;
import org.example.learningcenter.entity.dto.user.UserCreateDto;
import org.example.learningcenter.entity.dto.user.UserDto;
import org.example.learningcenter.entity.dto.user.UserUpdateDto;
import org.example.learningcenter.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<UserDto> users = userService.getAll(pageable, search);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable String id) {
        UserDto user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateDto createDto) {
        UserDto createdUser = userService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDto updateDto
    ) {
        UserDto updatedUser = userService.update(updateDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}