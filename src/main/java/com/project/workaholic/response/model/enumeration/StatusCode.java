package com.project.workaholic.response.model.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum StatusCode {
    /* 200 Success */
    SUCCESS(OK, ""),
    SUCCESS_LOGIN(OK, "정상적으로 로그인 되었습니다."),
    SUCCESS_LOGOUT(OK,"정상적으로 로그아웃 되었습니다."),
    SUCCESS_SIGNUP(OK,"정상적으로 회원가입 되었습니다."),
    SUCCESS_DELETE_ACCOUNT(OK,"정상적으로 회원탈퇴 되었습니다."),
    SUCCESS_UPDATE_ACCOUNT(OK,"정상적으로 회원 정보가 수정되었습니다."),
    SUCCESS_TRACKER(OK, "정상적으로 이슈가 연결 되었습니다."),

    /* 400 Bad Request*/
    INVALID_DATA_OBJECT(BAD_REQUEST, "요청 값이 유효하지 않습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    StatusCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
