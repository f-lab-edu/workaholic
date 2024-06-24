package com.project.workaholic.response.model.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum StatusCode {
    /* 200 Success */
    SUCCESS_LOGIN(OK, "정상적으로 로그인 되었습니다."),
    SUCCESS_LOGOUT(OK,"정상적으로 로그아웃 되었습니다."),
    SUCCESS_SIGNUP(OK,"정상적으로 회원가입 되었습니다."),
    SUCCESS_DELETE_ACCOUNT(OK,"정상적으로 회원탈퇴 되었습니다."),
    SUCCESS_UPDATE_ACCOUNT(OK,"정상적으로 회원 정보가 수정되었습니다."),
    SUCCESS_READ_PODS(OK,"정상적으로 Pod 목록이 조회하였습니다."),
    SUCCESS_READ_POD(OK,"정상적으로 Pod 조회하였습니다."),
    SUCCESS_DELETE_POD(OK,"정상적으로 Pod 삭제되었습니다."),
    SUCCESS_UPDATE_POD(OK,"정상적으로 Pod 설정이 수정되었습니다."),
    SUCCESS_DEPLOY_POD(OK,"정상적으로 Pod 로 배포되었습니다."),
    SUCCESS_READ_ALL_ISSUE_LIST(OK,"정상적으로 이슈 전체 목록을 조회하였습니다."),
    SUCCESS_READ_ISSUE_LIST_BY_PROJECT(OK,"정상적으로 해당 프로젝트의 이슈 목록을 조회하였습니다."),
    SUCCESS_READ_ISSUE(OK,"정상적으로 이슈를 조회하였습니다."),
    SUCCESS_CREATE_ISSUE(OK,"정상적으로 이슈를 생성하였습니다."),
    SUCCESS_DELETE_ISSUE(OK,"정상적으로 이슈를 삭제하였습니다."),
    SUCCESS_UPDATE_ISSUE(OK,"정상적으로 이슈를 수정하였습니다."),
    SUCCESS_AUTH_VCS(OK, "정상적으로 인증되었습니다."),
    SUCCESS_READ_REPO_LIST(OK, "정상적으로 Repository 목록이 조회되었습니다."),
    SUCCESS_IMPORT_REPO(OK, "정상적으로 Repository 가져왔습니다."),
    SUCCESS_READ_COMMIT_LIST(OK, "정상적으로 Commit 기록을 가져왔습니다."),
    SUCCESS_READ_BRANCHES(OK, "정상적으로 Repository Branch 목록을 가져왔습니다."),
    SUCCESS_READ_PROJECT(OK,"정상적으로 프로젝트를 가져왔습니다."),
    SUCCESS_READ_PROJECT_LIST(OK,"정상적으로 프로젝트 목록을 가져왔습니다."),
    SUCCESS_CREATE_PROJECT(OK,"정상적으로 프로젝트를 생성했습니다."),
    SUCCESS_UPDATE_PROJECT(OK,"정상적으로 프로젝트를 수정했습니다."),
    SUCCESS_DELETE_PROJECT(OK,"정상적으로 프로젝트를 삭제했습니다."),

    /* 400 Bad Request */
    INVALID_DATA_OBJECT(BAD_REQUEST, "요청 값이 유효하지 않습니다"),
    INVALID_ACCOUNT(BAD_REQUEST, "계정 정보가 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(BAD_REQUEST,"토큰의 형식이 유효하지 않습니다."),
    EXPIRED_TOKEN(BAD_REQUEST,"토큰의 기간이 만료되었습니다."),
    NON_SUPPORTED_VENDOR(BAD_REQUEST, "지원하지 않는 VCS 타입입니다."),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED_REQUEST(UNAUTHORIZED,"인증되지 않은 요청입니다."),

    /* 404 Not Found */
    NOT_FOUND_OAUTH_ACCESS_TOKEN(NOT_FOUND, "요청한 계정에 대한 액세스 토큰을 찾을 수 없습니다."),

    NOT_FOUND_PROJECT(NOT_FOUND, "해당 프로젝트는 존재하지 않습니다."),

    /* 409 Conflict */
    EXISTS_ACCOUNT_ID(CONFLICT, "이미 존재하는 아이디 입니다."),
    EXISTS_PROJECT_ID(CONFLICT, "이미 존재하는 프로젝트 아이디 입니다."),

    /* 500 Internal Error */
    ERROR(INTERNAL_SERVER_ERROR, "에러")
    ;
    private final HttpStatus httpStatus;
    private final String message;

    StatusCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
