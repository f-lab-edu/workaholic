package com.project.workaholic.account.model;

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
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @JsonView(JsonViewsConfig.POST.class)
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    @Schema(description = "아이디", requiredMode = Schema.RequiredMode.REQUIRED, example = "temp123")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Schema(description = "이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "김이름")
    private String name;

    @NotBlank(message = "메일 주소는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Schema(description = "메일 주소", requiredMode = Schema.RequiredMode.REQUIRED, example = "example@gmail.com")
    private String mail;

    @Builder
    public AccountRegisterDto(String id, String password, String name, String mail) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.mail = mail;
    }
}
