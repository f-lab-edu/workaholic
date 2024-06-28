package com.project.workaholic.config.interceptor;

import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.model.entity.RefreshToken;
import com.project.workaholic.account.repository.RefreshTokenRepository;
import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "token")
public class JsonWebTokenProvider {
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    private final Key key;
    private final String SECRET;
    private final long ACCESS_EXP;
    private final long REFRESH_EXP;

    public JsonWebTokenProvider(Key key, String SECRET, long ACCESS_EXP, long REFRESH_EXP) {
        this.SECRET = SECRET;
        this.ACCESS_EXP = ACCESS_EXP * 60 * 1000;
        this.REFRESH_EXP = REFRESH_EXP * 24 * 60 * 60 * 1000;
        // 24 Hour * 60 Min * 60 Sec * 1000 Mils
        this.key = Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Date getTokenExpiration(long expirationMillisecond) {
        Date date = new Date();
        return new Date(date.getTime() + expirationMillisecond);
    }

    /**
     * 고유한 값(=계정 ID)을 통해서 RefreshToken 생성
     * @param authUserId Account 데이터로 Auth 를 위해서 UserDetails 구현체의 고유값(=계정 ID)
     * @return refreshToken 문자열
     */
    private String generateRefreshToken(String authUserId) {
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(authUserId)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpiration(REFRESH_EXP))
                .compact();
    }

    /**
     * 고유한 값(=계정 ID)을 통해서 AccessToken 생성
     * @param authUserId Account 데이터로 Auth 를 위해서 UserDetails 구현체의 고유값(=계정 ID)
     * @param claims AccessToken 에 첨부할 비즈니스 로직을 위한 데이터
     * @return AccessToken 문자열
     */
    private String generateAccessToken(String authUserId, Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(claims == null ? new HashMap<>() : claims)
                .setSubject(authUserId)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpiration(ACCESS_EXP))
                .compact();
    }

    /**
     * 유저가 정상적으로 Login 될 경우 기본적인 데이터(id,name) 만 포함한 토큰 생성
     * 만약 리프레쉬 토큰이 존재하는데 Logout 상태 일 경우 새로운 토큰을 만들 수 없어야 함
     * @param account Account 객체
     * @return JsonWebToken
     */
    public JsonWebToken generateBasicToken(Account account) {
        String accessToken = generateAccessToken(account.getId(), null);
        String refreshToken = generateRefreshToken(account.getId());

        return JsonWebToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * HttpServletRequest 로 부터 사전에 정의된 헤더에서 AccessToken 추출
     * @param request HttpServletRequest
     * @return AccessToken 없을 경우 null
     */
    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    /**
     * 전달된 Token 검증
     * @param token 토큰
     * @return 검증 결과 성공시 true / 실패 시 exception
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new CustomException(StatusCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e){
            throw new CustomException(StatusCode.EXPIRED_TOKEN);
        }
        return true;
    }
}
