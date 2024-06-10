package com.project.workaholic.vcs.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OauthPrincipalUserService extends DefaultOAuth2UserService {

    /**
     * OAuth user request 데이터에 대한 처리 함수
     * @param userRequest OAuth Request
     * @return OAuth 로 받게되는 정보
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }
}
