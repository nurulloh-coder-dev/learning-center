package org.example.crm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    InvalidParams(400),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    InvalidDevice(404),
    NotFound(404),
    InternalServerError(500),
    AlreadyExists(409),
    TooManyRequests(429);

    private final int statusCode;
}
