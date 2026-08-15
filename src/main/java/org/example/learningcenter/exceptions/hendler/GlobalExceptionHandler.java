package org.example.learningcenter.exceptions.hendler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorMessage;
import org.example.learningcenter.exceptions.RestException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleCustomException(RestException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getErrorCode().getStatusCode());
        Locale locale = resolveLocale(request);

        String errorMessage = ex.getType() == null ? ex.getMessage() : messageSource.getMessage(
                ex.getType().getKey(),
                ex.getArgs(),
                "MessageKey not found: " + ex.getType().getKey(),
                locale
        );

        return ResponseEntity.status(httpStatus).body(
                ErrorMessage.of(ex.getErrorCode().name(), errorMessage, request)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN) // 403
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(FORBIDDEN).body(ErrorMessage.of(ErrorCodes.Forbidden.name(), "Forbidden", request));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND) // 404
    public ResponseEntity<?> dataFormatException(NoHandlerFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(ErrorMessage.of(ErrorCodes.NotFound.name(), "Not found", request));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(NOT_FOUND) // 404
    public ResponseEntity<?> noResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(ErrorMessage.of(ErrorCodes.NotFound.name(), "Not found", request));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR) // 500
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ErrorMessage.of(ErrorCodes.InternalServerError.name(), ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });
        return ResponseEntity.badRequest().body(errors);
    }


    private Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        if (language == null || language.isBlank()) return new Locale("uz");
        if (language.equals("ru")) return new Locale("ru");
        if (language.equals("uz")) return new Locale("uz");
        return Locale.ENGLISH;
    }
}
