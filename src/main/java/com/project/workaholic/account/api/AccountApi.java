package com.project.workaholic.account.api;

import com.project.workaholic.account.model.AccountSignIdDto;
import com.project.workaholic.account.model.AccountRegisterDto;
import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.service.AccountService;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account API", description = "Workaholic 계정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountApi {
    private final AccountService accountService;

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
            @Valid @RequestBody AccountSignIdDto accountLoginDto) {
        Account accountToVerify = toEntity(accountLoginDto);
        accountToVerify = accountService.verifyAccount(accountToVerify);
        return ApiResponse.success(StatusCode.SUCCESS_LOGIN, toSignIdDto(accountToVerify));
    }

    @Operation(summary = "계정 로그아웃", description = "Workaholic Service 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<StatusCode>> logout() {
        return ApiResponse.success(StatusCode.SUCCESS_LOGOUT);
    }

    @Operation(summary = "계정 회원가입", description = "Workaholic Service 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<StatusCode>> signup(
            final @Parameter(name = "회원가입 폼", description = "회원가입을 위한 계정 정보")
            @Valid @RequestBody AccountRegisterDto accountRegisterDto) {
        Account accountToRegister = toEntity(accountRegisterDto);
        accountService.registerAccount(accountToRegister);
        return ApiResponse.success(StatusCode.SUCCESS_SIGNUP);
    }

    @Operation(summary = "계정 삭제", description = "Workaholic Service 탈퇴")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<StatusCode>> deleteAccount() {
        return ApiResponse.success(StatusCode.SUCCESS_DELETE_ACCOUNT);
    }

    @Operation(summary = "계정 수정", description = "Workaholic Service 계정 정보 변경")
    @PutMapping("")
    public ResponseEntity<ApiResponse<StatusCode>> updateAccount() {
        return ApiResponse.success(StatusCode.SUCCESS_UPDATE_ACCOUNT);
    }
}
