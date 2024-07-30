package com.project.oauth.service;

import com.project.oauth.model.VCSRepository;
import com.project.oauth.model.entity.OAuthAccessToken;

public interface VendorApiService {
    void init();
    OAuthAccessToken getOAuthAccessTokenByAccountId(String accountId);
    VCSRepository getRepositoryInformation(String accessToken, String repositoryName);
}