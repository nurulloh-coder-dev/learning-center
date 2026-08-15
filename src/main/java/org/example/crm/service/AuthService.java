package org.example.crm.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.config.JwtUtils;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.login.LoginRequest;
import org.example.crm.entity.login.LoginResponse;
import org.example.crm.entity.login.TokenDto;
import org.example.crm.entity.model.User;
import org.example.crm.entity.request.ChangePasswordRequest;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.UserMapper;
import org.example.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Value("${jwt.refresh.token.expire.date:86400}")
    private Long refreshTokenExpiration;

    final PasswordEncoder passwordEncoder;





    public LoginResponse getLoginResponseResponseEntity(LoginRequest request,HttpServletResponse response) {
        String phone = request.getPhone();

        User user = userRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() ->new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest);
        }

        Map<String, Object> claims = jwtUtils.prepareClaims(user);
        TokenDto accessToken = jwtUtils.generateToken(user.getPhone(), claims, "access");
        TokenDto refreshToken = jwtUtils.generateToken(user.getPhone(), claims, "refresh");
        setRefreshCookie(response,refreshToken.getToken());

        return LoginResponse.builder()
                .token(accessToken.getToken())
                .expiry(accessToken.getExpiry())
                .build();
    }


    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RestException(ErrorType.REFRESH_TOKEN_NOT_FOUND, ErrorCodes.NotFound);
        }

        String oldRefreshToken = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RestException(ErrorType.REFRESH_TOKEN_NOT_FOUND, ErrorCodes.NotFound));

        Claims claims = jwtUtils.extractClaimsIgnoreExpiry(oldRefreshToken);
        String phone = claims.getSubject();

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RestException(ErrorType.PHONE_NUMBER_NOT_FOUND, ErrorCodes.NotFound));

        TokenDto access = jwtUtils.generateToken(phone, jwtUtils.prepareClaims(user), "access");

        Map<String, Object> refreshClaims = jwtUtils.prepareClaims(user);
        TokenDto refresh = jwtUtils.generateToken(phone, refreshClaims, refreshTokenExpiration);

        setRefreshCookie(response, refresh.getToken());

        return LoginResponse.builder()
                .token(access.getToken())
                .expiry(access.getExpiry())
                .build();
    }

    private void setRefreshCookie(HttpServletResponse response, String tokenValue) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refresh_token", tokenValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .sameSite("Lax");

        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

    public String changePassword(@Valid ChangePasswordRequest request, User user) {

        if (request.confirmPassword().equals(request.newPassword())) {
            throw new RestException(ErrorType.PASSWORDS_DO_NOT_MATCH, ErrorCodes.BadRequest);
        }

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    public UserDto getMe(User user) {
        return userMapper.toDto(user);
    }
}
