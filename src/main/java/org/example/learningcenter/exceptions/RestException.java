package org.example.learningcenter.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestException extends RuntimeException {

    private ErrorType type;
    private ErrorCodes errorCode;
    private Object[] args;
    private String message;
    private int statusCode;

    public RestException(ErrorType type, ErrorCodes errorCode, Object... args) {
        super(type != null ? type.getKey() : null); // important: sets RuntimeException message
        this.type = type;
        this.errorCode = errorCode;
        this.args = args == null ? new Object[0] : args;
        this.message = (type != null ? type.getKey() : null);
    }

    public RestException(String message, ErrorCodes codes) {
        super(message);
        this.message = message;
        this.errorCode = codes;
        this.args = new Object[0];
    }

    public RestException(String message, ErrorCodes codes, Object... args) {
        super(message);
        this.message = message;
        this.errorCode = codes;
        this.args = args == null ? new Object[0] : args;
    }

    public RestException(String errMsg, int statusCode){
        this.message = errMsg;
        this.statusCode = statusCode;
    }
}