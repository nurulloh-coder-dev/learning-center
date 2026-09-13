package org.example.crm.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crm.config.JwtUtils;
import org.example.crm.entity.dto.IdNameDto;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.enums.Role;
import org.example.crm.entity.login.LoginRequest;
import org.example.crm.entity.login.LoginResponse;
import org.example.crm.entity.login.TokenDto;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.User;
import org.example.crm.entity.request.ChangePasswordRequest;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.UserMapper;
import org.example.crm.repository.OrganizationRepository;
import org.example.crm.repository.StudentRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AuthService {


    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    final OrganizationRepository organizationRepository;
    final StudentRepository studentRepository;

    @Value("${jwt.refresh.token.expire.date:86400}")
    private Long refreshTokenExpiration;

    final PasswordEncoder passwordEncoder;

    public LoginResponse getLoginResponseResponseEntity(@Valid LoginRequest request, HttpServletResponse response) {
        log.info("{} is trying to log in", request.getPhone());
        String phone = request.getPhone();

        User user = userRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() -> new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest);
        }

        List<Student> studentList = studentRepository.findAllByUserId(user.getId());

        if (user.getRole().equals(Role.STUDENT)&& !studentList.isEmpty()) {
            if (studentList.size() > 1) {
                List<String> orgId = studentList.stream().map(Student::getOrganizationId).toList();
                List<IdNameDto> idNameDtos = organizationRepository.findAllById(orgId).stream().map(
                        organization -> IdNameDto.builder()
                                .id(organization.getId())
                                .name(organization.getName())
                                .build()).toList();

                return LoginResponse.builder()
                        .token(null)
                        .expiry(null)
                        .requiresOrganizationSelection(true)
                        .organizations(idNameDtos)
                        .build();
            }

            Student student = studentList.get(0);
            if (!student.getOrganizationId().equals(user.getOrganizationId())) {
                throw new RestException(ErrorType.WRONG_ORGANIZATION, ErrorCodes.Forbidden);
            }

        }

        return getLoginResponse(user.getOrganizationId(), response, user);


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

        String organizationId = claims.get("organizationId", String.class);

        TokenDto access = jwtUtils.generateToken(phone, jwtUtils.prepareClaims(user, organizationId), "access");

        Map<String, Object> refreshClaims = jwtUtils.prepareClaims(user, organizationId);
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

    public String changePassword(@Valid ChangePasswordRequest request) {
        User user = userValidator.authenticateAndGetUser();
        if (!request.confirmPassword().equals(request.newPassword())) {
            throw new RestException(ErrorType.PASSWORDS_DO_NOT_MATCH, ErrorCodes.BadRequest);
        }

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    public UserDto getMe() {
        User user = userValidator.authenticateAndGetUser();
        return userMapper.toDto(user);
    }

    public LoginResponse selectOrganization(String organizationId, LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByPhoneAndDeletedFalse(request.getPhone())
                .orElseThrow(() -> new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RestException(ErrorType.INVALID_PHONE_NUMBER_OR_PASSWORD, ErrorCodes.BadRequest);
        }

        boolean b = organizationRepository.existsById(organizationId);
        if (!b) {
            throw new RestException(ErrorType.ORGANIZATION_NOT_FOUND, ErrorCodes.NotFound);
        }
        studentRepository.findStudentByOrganizationIdAndUserId(organizationId, user.getId())
                .orElseThrow(() -> new RestException(ErrorType.STUDENT_NOT_FOUND, ErrorCodes.NotFound));


        return getLoginResponse(organizationId, response, user);
    }

    private LoginResponse getLoginResponse(String organizationId, HttpServletResponse response, User user) {
        Map<String, Object> claims = jwtUtils.prepareClaims(user, organizationId);
        TokenDto accessToken = jwtUtils.generateToken(user.getPhone(), claims, "access");
        TokenDto refreshToken = jwtUtils.generateToken(user.getPhone(), claims, "refresh");
        setRefreshCookie(response, refreshToken.getToken());


        return LoginResponse.builder()
                .token(accessToken.getToken())
                .expiry(accessToken.getExpiry())
                .requiresOrganizationSelection(false)
                .organizations(null)
                .build();
    }
}
