package com.hg.blog.exception;

import java.security.AccessControlException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionResolver {

    /**
     * spring security 인증 error
     *
     * @param ex : 권한 에러
     * @return ErrorResponse
     */
    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(RuntimeException ex) {
        log.error("security error ==> {}", ex.getMessage());
        log.error("security error ==> {}", ex.getClass().getName());
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(value = AccessControlException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse accessControlExceptionHandler(AccessControlException ex) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(exception ->
                String.format("[%s](은)는 %s. 입력된 값: [%s]",
                    exception.getField(),
                    exception.getDefaultMessage(),
                    exception.getRejectedValue()
                )
            )
            .collect(Collectors.joining(", "));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }
}
