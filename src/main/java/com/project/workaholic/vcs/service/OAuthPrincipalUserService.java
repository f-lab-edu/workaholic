package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.repository.OAuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthPrincipalUserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final OAuthAccountRepository oAuthAccountRepository;
    /**
     * OAuth user request 데이터에 대한 처리 함수
     * @param userRequest OAuth Request
     * @return OAuth 로 받게되는 정보
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("userRequest = " + userRequest);
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken());
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
        return super.loadUser(userRequest);
    }
}
