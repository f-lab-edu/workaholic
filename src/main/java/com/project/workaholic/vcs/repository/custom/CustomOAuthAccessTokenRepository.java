package com.project.workaholic.vcs.repository.custom;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;

import java.util.Optional;

public interface CustomOAuthAccessTokenRepository {
    Optional<OAuthAccessToken> findGithubByAccountId(String accountId);
}
