package com.project.workaholic.account.api;

import com.project.workaholic.account.model.AccountSignIdDto;
import com.project.workaholic.account.model.AccountRegisterDto;
import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.service.AccountService;
import com.project.workaholic.config.interceptor.JsonWebToken;
import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.config.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account API", description = "Workaholic 계정 API")
@RestController
@RequestMapping("/account")
public class AccountApi {
    private final AccountService accountService;
    private final JsonWebTokenProvider jsonWebTokenProvider;

    public AccountApi(AccountService accountService, JsonWebTokenProvider jsonWebTokenProvider) {
        this.accountService = accountService;
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    private Account toEntity(AccountSignIdDto dto) {
        return Account.builder()
                .id(dto.getId())
                .password(dto.getPassword())
                .build();
    }

    private Account toEntity(AccountRegisterDto dto) {
        return Account.builder()
                .id(dto.getId())
                .password(dto.getPassword())
                .name(dto.getName())
                .build();
    }

    private AccountSignIdDto toSignIdDto(Account account) {
        return AccountSignIdDto.builder()
                .id(account.getId())
                .name(account.getName())
                .build();
    }

    @Operation(summary = "계정 로그인", description = "Workaholic Service 로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccountSignIdDto>> login(
            final @Parameter(name = "로그인 폼", description = "로그인을 위한 계정 정보")
            @Valid @RequestBody AccountSignIdDto accountLoginDto,
            HttpServletResponse response) {
        Account accountToVerify = toEntity(accountLoginDto);
        accountToVerify = accountService.verifyAccount(accountToVerify);
        JsonWebToken jsonWebToken = jsonWebTokenProvider.generateBasicToken(accountToVerify);

        response.setHeader(HttpHeaders.AUTHORIZATION, jsonWebToken.getAccessToken());
        response.setHeader("Refresh-Token", jsonWebToken.getRefreshToken());

        Cookie refreshTokenCookie = new Cookie("Refresh-Token", jsonWebToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24); // 1 day
        refreshTokenCookie.setDomain("example.com"); // Set the domain if needed
        response.addCookie(refreshTokenCookie);

        return ApiResponse.success(toSignIdDto(accountToVerify));
    }

    @Operation(summary = "계정 로그아웃", description = "Workaholic Service 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ApiResponse.success();
    }

    @Operation(summary = "계정 회원가입", description = "Workaholic Service 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            final @Parameter(name = "회원가입 폼", description = "회원가입을 위한 계정 정보")
            @Valid @RequestBody AccountRegisterDto accountRegisterDto) {
        Account accountToRegister = toEntity(accountRegisterDto);
        accountService.registerAccount(accountToRegister);
        return ApiResponse.success();
    }

    @Operation(summary = "계정 삭제", description = "Workaholic Service 탈퇴")
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteAccount() {
        return ApiResponse.success();
    }

    @Operation(summary = "계정 수정", description = "Workaholic Service 계정 정보 변경")
    @PutMapping("")
    public ResponseEntity<Void> updateAccount() {
        return ApiResponse.success();
    }
}
