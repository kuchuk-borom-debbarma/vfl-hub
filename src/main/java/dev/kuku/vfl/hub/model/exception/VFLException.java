package dev.kuku.vfl.hub.model.exception;

import org.springframework.http.HttpStatus;

public class VFLException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String message;

    public VFLException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
