package com.hg.blog.exception;

import java.security.AccessControlException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = AccessControlException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse accessControlExceptionHandler(HttpServletRequest request,
        AccessControlException ex) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(HttpServletRequest request,
        IllegalArgumentException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex,
        WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(exception -> String.format("[%s](은)는 %s. 입력된 값: [%s]", exception.getField(),
                exception.getDefaultMessage(), exception.getRejectedValue())
            )
            .collect(
                Collectors.joining(", "));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }
}
