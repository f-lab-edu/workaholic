package com.project.workaholic.config.interceptor;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JsonWebToken {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public JsonWebToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
