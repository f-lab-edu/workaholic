package com.project.workaholic.config.exception;

import com.project.workaholic.config.exception.type.DuplicateAccountException;
import com.project.workaholic.config.exception.type.DuplicateProjectException;
import com.project.workaholic.config.exception.type.ExpiredTokenException;
import com.project.workaholic.config.exception.type.FailedOAuthToken;
import com.project.workaholic.config.exception.type.InvalidAccessTokenException;
import com.project.workaholic.config.exception.type.InvalidAccountException;
import com.project.workaholic.config.exception.type.NonSupportedAlgorithmException;
import com.project.workaholic.config.exception.type.NotFoundAccountException;
import com.project.workaholic.config.exception.type.NotFoundOAuthTokenException;
import com.project.workaholic.config.exception.type.NotFoundProjectException;
import com.project.workaholic.config.exception.type.NotSetTemplateModelException;
import com.project.workaholic.config.exception.type.UnauthorizedRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidAccountException.class)
    protected ResponseEntity<ExceptionResponse> handleInvalidAccountException(InvalidAccountException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "계정 정보가 유효하지 않습니다."));
    }

    @ExceptionHandler(NotFoundAccountException.class)
    protected ResponseEntity<ExceptionResponse> handleNotFoundAccountException(NotFoundAccountException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), "해당 계정은 존재하지 않습니다."));
    }

    @ExceptionHandler(NotFoundOAuthTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleNotFoundOAuthTokenException(NotFoundOAuthTokenException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), "해당 계정은 " + e.getVendor() +"의 OAuth Token 존재하지 않습니다."));
    }

    @ExceptionHandler(NotFoundProjectException.class)
    protected ResponseEntity<ExceptionResponse> handleNotFoundProjectException(NotFoundProjectException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), "해당 프로젝트는 존재하지 않습니다."));
    }

    @ExceptionHandler(ExpiredTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleExpiredTokenException(ExpiredTokenException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "토큰의 기간이 만료되었습니다."));
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleInvalidAccessTokenException(InvalidAccessTokenException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "토큰의 형식이 유효하지 않습니다."));
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    protected ResponseEntity<ExceptionResponse> handleUnauthorizedRequestException(UnauthorizedRequestException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "인증되지 않은 요청입니다."));
    }

    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<ExceptionResponse> handleDuplicateProjectException(DuplicateAccountException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(HttpStatus.CONFLICT.getReasonPhrase(), "이미 존재하는 메일 주소 입니다."));
    }

    @ExceptionHandler(DuplicateProjectException.class)
    protected ResponseEntity<ExceptionResponse> handleDuplicateProjectException(DuplicateProjectException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(HttpStatus.CONFLICT.getReasonPhrase(), "이미 존재하는 프로젝트 입니다."));
    }

    @ExceptionHandler(FailedOAuthToken.class)
    protected ResponseEntity<ExceptionResponse> handleFailedOAuthToken(FailedOAuthToken e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getVendor() + "의 OAuth Token 발급도중 문제가 발생하였습니다."));
    }

    @ExceptionHandler(NonSupportedAlgorithmException.class)
    protected ResponseEntity<ExceptionResponse> handleNonSupportedAlgorithmException(NonSupportedAlgorithmException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getAlgorithmName() + "이 지원되지 않습니다."));
    }

    @ExceptionHandler(NotSetTemplateModelException.class)
    protected ResponseEntity<ExceptionResponse> handleNotSetTemplateModelException(NotSetTemplateModelException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Docker 파일을 생성하기 위한 Template 의 Model 이 설정되어있지 않습니다."));
    }

    private static List<FieldException> toList(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error ->
                        new FieldException(error.getField(),
                                error.getRejectedValue() != null
                                        ? error.getRejectedValue().toString()
                                        : null,
                                error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponseWithField(HttpStatus.BAD_REQUEST.getReasonPhrase(), "", toList(ex.getBindingResult())));
    }
}
