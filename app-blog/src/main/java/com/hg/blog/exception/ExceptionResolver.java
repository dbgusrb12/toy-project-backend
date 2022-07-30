package com.hg.blog.exception;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ErrorResponse illegalArgumentExceptionHandler(HttpServletRequest request,
        IllegalArgumentException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(exception -> String.format("[%s](은)는 %s. 입력된 값: [%s]", exception.getField(),
                exception.getDefaultMessage(), exception.getRejectedValue())
            )
            .collect(
                Collectors.joining(", "));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }
}
