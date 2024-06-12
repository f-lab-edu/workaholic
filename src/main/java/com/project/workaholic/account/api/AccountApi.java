package com.project.workaholic.account.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.workaholic.account.model.AccountLoginDto;
import com.project.workaholic.account.model.AccountRegisterDto;
import com.project.workaholic.config.JsonViewsConfig;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account API", description = "Workaholic 계정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountApi {

    @Operation(summary = "계정 로그인", description = "Workaholic Service 로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<StatusCode>> login(
            final @Parameter(name = "로그인 폼", description = "로그인을 위한 계정 정보")
            @Valid @RequestBody AccountLoginDto accountLoginDto) {
        return ApiResponse.success(StatusCode.SUCCESS_LOGIN);
    }

    @Operation(summary = "계정 로그아웃", description = "Workaholic Service 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<StatusCode>> logout(
            final @Parameter(name = "로그인 계정 정보", description = "로그인으로 받은 토큰")
            @Valid @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ApiResponse.success(StatusCode.SUCCESS_LOGOUT);
    }

    @Operation(summary = "계정 회원가입", description = "Workaholic Service 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<StatusCode>> signup(
            final @Parameter(name = "회원가입 폼", description = "회원가입을 위한 계정 정보")
            @Valid @RequestBody AccountRegisterDto accountRegisterDto) {
        return ApiResponse.success(StatusCode.SUCCESS_SIGNUP);
    }

    @Operation(summary = "계정 삭제", description = "Workaholic Service 탈퇴")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<StatusCode>> deleteAccount(
            final @Parameter(name = "로그인 계정 정보", description = "로그인으로 받은 토큰")
            @Valid @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            final @Parameter(name = "로그인 폼", description = "로그인에 사용된 계정 정보")
            @Valid @RequestBody AccountRegisterDto accountRegisterDto) {
        return ApiResponse.success(StatusCode.SUCCESS_DELETE_ACCOUNT);
    }

    @Operation(summary = "계정 수정", description = "Workaholic Service 계정 정보 변경")
    @PutMapping("")
    public ResponseEntity<ApiResponse<StatusCode>> updateAccount(
            final @Parameter(name = "로그인 계정 정보", description = "로그인으로 받은 토큰")
            @Valid @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            final @Parameter(name = "회원가입에 사용된 폼", description = "회원가입에 사용한 계정 정보")
            @Valid @RequestBody @JsonView(JsonViewsConfig.PUT.class) AccountRegisterDto accountRegisterDto) {
        return ApiResponse.success(StatusCode.SUCCESS_UPDATE_ACCOUNT);
    }
}
