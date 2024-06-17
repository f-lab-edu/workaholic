package com.project.workaholic.config.interceptor;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final JsonWebTokenProvider jsonWebTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = jsonWebTokenProvider.extractAccessToken(request);
        if( !StringUtils.hasText(accessToken) ) {
            throw new CustomException(StatusCode.UNAUTHORIZED_REQUEST);
        }

        if(jsonWebTokenProvider.validateToken(accessToken)) {
            Claims claims = jsonWebTokenProvider.parseClaims(accessToken);
        }

        return true;
    }
}
