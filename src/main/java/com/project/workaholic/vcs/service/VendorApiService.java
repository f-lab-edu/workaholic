package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;

public interface VendorApiService {
    void init();
    OAuthAccessToken getOAuthAccessTokenByAccountId(String accountId);
}
