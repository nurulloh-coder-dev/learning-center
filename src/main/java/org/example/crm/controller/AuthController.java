package org.example.crm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.annotation.CurrentUser;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.login.LoginRequest;
import org.example.crm.entity.login.LoginResponse;
import org.example.crm.entity.model.User;
import org.example.crm.entity.request.ChangePasswordRequest;
import org.example.crm.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponseResponseEntity = authService.getLoginResponseResponseEntity(request, response);
        return ResponseEntity.ok(loginResponseResponseEntity);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request,
                                                      HttpServletResponse response) {
        LoginResponse result = authService.refreshToken(request, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                               @CurrentUser User user) {
        String response =  authService.changePassword(request, user);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @GetMapping("/me")
    public  ResponseEntity<UserDto> me(@CurrentUser User user) {
        return ResponseEntity.ok(authService.getMe(user));
    }
}
