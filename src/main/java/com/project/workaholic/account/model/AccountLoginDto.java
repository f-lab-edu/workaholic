package com.project.workaholic.account.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountLoginDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Schema(description = "아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Builder
    public AccountLoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
