package com.project.workaholic.account.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.workaholic.config.JsonViewsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountRegisterDto {
    @NotBlank(message = "아이디(메일주소) 는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "메일 형식에 일치하지 않습니다.")
    @Schema(description = "아이디", requiredMode = Schema.RequiredMode.REQUIRED, example = "test@example.com")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "password12!")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Schema(description = "이름", example = "김유저")
    private String name;

    @Builder
    public AccountRegisterDto(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}
