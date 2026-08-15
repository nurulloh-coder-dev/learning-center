package org.example.crm.exceptions;

import lombok.Getter;

@Getter
public enum ErrorType {
    INTERNAL_ERROR("internal.server.error"),
    FILE_TYPE_ERROR("file.type.error"),
    ERROR_SAVING_FILE("error.saving.file"),
    BAD_JSON("bad.json"),
    PAGE_NOT_FOUND("page.not.found"),
    USER_NOT_FOUND("user.not.found"),
    LESSON_NOT_FOUND("lesson,not.found"),
    ENROLLMENT_NOT_FOUND("enrollment.not.found"),
    ATTENDANCE_NOT_FOUND("attendance.not.found"),
    UNSUPPORTED_MEDIA_TYPE("unsupported.media.type"),
    METHOD_NOT_ALLOWED("method.not.allowed"),
    MISSING_PARAMETER("missing.parameter"),
    TYPE_MISMATCH("type.mismatch"),
    VALIDATION_ERROR("validation.error"),
    UNAUTHORIZED("unauthorized"),
    FORBIDDEN("forbidden"),
    PHONE_NUMBER_ALREADY_EXISTS("phone.number.alerady.exists"),
    USER_ALREADY_EXISTS("user.alerady.exists"),
    ILLEGAL_USER("illegal.user"),
    INVALID_PHONE_NUMBER_OR_PASSWORD("illegal.phone.number.or.password"),
    PHONE_NUMBER_NOT_FOUND("phone.number.not.found"),
    USER_IS_BLOCKED("user.is.blocked"),
    NO_PERMISSION("no.permission"),
    ACCESS_DENIED("acces.denied"),
    USER_NOT_MATCH("user.not.much"),
    REFRESH_TOKEN_NOT_FOUND("refresh.token.not.found"),
    TEST_NOT_FOUND("test.not.found"),
    ATTACHMENT_NOT_FOUND("attachment.not.found"),
    REFRESH_TOKEN_EXPIRED("refresh.token.expired"),
    GROUP_ALREADY_EXISTS_WITH_THIS_NAME("group.already.exists.with.this.name"),
    TEACHER_NOT_FOUND("teacher.not.found"),
    TIMETABLE_NOT_FOUND("timetable.not.found"),
    GROUP_NOT_FOUND("group.not.found"),
    INVALID_TIME_RANGE("invalid.time.range"),
    INVALID_FILE_TYPE("invalid.file.type"),
    INVOICE_NOT_FOUND("invoice.not.found"),
    STUDENT_NOT_FOUND("student.not.found"),
    INVOICE_COUNTER_NOT_FOUND("invoice.counter.not.found"),
    PASSWORDS_DO_NOT_MATCH("passwords.do.not.match"),
    BRANCH_NOT_FOUND("branch.not.found"),
    BRANCH_ALREADY_EXISTS("branch.already.exists"),
    ORGANIZATION_NOT_FOUND("organization.not.found"),
    ORGANIZATION_ALREADY_EXISTS("organization.already.exists");


    private final String key;

    ErrorType(String key) {
        this.key = key;
    }
}