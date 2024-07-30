package com.project.oauth.repository.custom;

import com.project.oauth.model.entity.OAuthAccessToken;

import java.util.Optional;

public interface CustomOAuthAccessTokenRepository {
    Optional<OAuthAccessToken> findGithubByAccountId(String accountId);
    Optional<OAuthAccessToken> findGitlabByAccountId(String accountId);
}
