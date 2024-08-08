package com.project.config.exception;

import com.project.config.exception.type.ExpiredTokenException;
import com.project.config.exception.type.UnauthorizedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ExpiredTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleExpiredTokenException(ExpiredTokenException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "토큰의 기간이 만료되었습니다."));
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    protected ResponseEntity<ExceptionResponse> handleUnauthorizedRequestException(UnauthorizedRequestException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "인증되지 않은 요청입니다."));
    }
}
