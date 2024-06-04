package com.project.workaholic.response.model.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum StatusCode {
    /* 200 Success */
    SUCCESS(OK, ""),
    LOGIN_SUCCESS(OK, "정상적으로 로그인되었습니다."),
    TRACKER_SUCCESS(OK, "정상적으로 이슈가 연결 되었습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    StatusCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
