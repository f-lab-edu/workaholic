package com.project.workaholic.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final JsonWebTokenProvider jsonWebTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = jsonWebTokenProvider.extractAccessToken(request);
//        if( !StringUtils.hasText(accessToken) ) {
//            throw new CustomException(StatusCode.UNAUTHORIZED_REQUEST);
//        }
//
//        if(jsonWebTokenProvider.validateToken(accessToken)) {
//            Claims claims = jsonWebTokenProvider.parseClaims(accessToken);
//        }

        return true;
    }
}
