package com.project.workaholic.config.interceptor;

import com.project.workaholic.config.exception.type.UnauthorizedRequestException;
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
            throw new UnauthorizedRequestException();
        }

        if(jsonWebTokenProvider.validateToken(accessToken)) {
            Claims claims = jsonWebTokenProvider.parseClaims(accessToken);
            request.setAttribute("id", claims.getSubject());
        }

        return true;
    }
}
