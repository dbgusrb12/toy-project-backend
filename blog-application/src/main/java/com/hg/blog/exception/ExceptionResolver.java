package com.hg.blog.exception;

import java.security.AccessControlException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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
        print("security Error: ", ex);
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NoHandlerFoundException ex) {
        print("Not Found Error: ", ex);
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(value = AccessControlException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse accessControlExceptionHandler(AccessControlException ex) {
        print("Access Control Error: ", ex);
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        print("Illegal Argument Error: ", ex);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        print("Valid Error: ", ex);
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

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationException(ConstraintViolationException ex) {
        print("Valid Error: ", ex);
        String message = ex.getConstraintViolations().stream()
            .map(constraintViolation -> {
                String fieldName = null;
                // property path 의 leaf node 가 field name
                for (Node node : constraintViolation.getPropertyPath()) {
                    fieldName = node.getName();
                }
                return String.format("[%s](은)는 %s. 입력된 값: [%s]",
                    fieldName,
                    constraintViolation.getMessage(),
                    constraintViolation.getInvalidValue()
                );
            })
            .collect(Collectors.joining(", "));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerException(Exception ex) {
        print("Internal Server Error: ", ex);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private void print(String format, Exception e) {
        log.error(format, e);
    }
}
