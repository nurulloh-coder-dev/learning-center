package org.example.crm.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private Timestamp timestamp;
    private String errorCode;
    private String message;
    private String path;

    public ErrorMessage(String errorCode, String message, String path) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }
    public static ErrorMessage of(String code, String message, HttpServletRequest request) {
        return new ErrorMessage(code, message, request.getRequestURI());
    }
}
