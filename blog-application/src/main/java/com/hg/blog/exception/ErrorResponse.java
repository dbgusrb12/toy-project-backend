package com.hg.blog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {
    private HttpStatus status;
    private int code;
    private String message;

    private ErrorResponse(HttpStatus status, RuntimeException ex) {
        this.status = status;
        this.code = status.value();
        this.message = ex.getMessage();
    }
    private ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.code = status.value();
        this.message = message;
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        return errorResponse;
    }

    public static ErrorResponse of(HttpStatus status, RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(status, ex);
        return errorResponse;
    }
}
