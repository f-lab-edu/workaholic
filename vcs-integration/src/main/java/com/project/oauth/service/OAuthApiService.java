package com.project.oauth.service;

import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.repository.OAuthAccessTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthApiService {
    private final VendorManager vendorManager;
    private final OAuthAccessTokenRepository tokenRepository;

    public OAuthApiService(VendorManager vendorManager, OAuthAccessTokenRepository tokenRepository) {
        this.vendorManager = vendorManager;
        this.tokenRepository = tokenRepository;
    }

    public VendorApiService getMatchServiceByVendor(VCSVendor vendor) {
        return vendorManager.getService(vendor);
    }

    public void registerToken(String accountId, String token, VCSVendor vendor) {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken(accountId, vendor, token);
        tokenRepository.save(oAuthAccessToken);
    }

    public void renewAccessToken(OAuthAccessToken existingToken, String token) {
        existingToken.setToken(token);
        tokenRepository.save(existingToken);
    }

    public OAuthAccessToken getOAuthAccessTokenByAccountId(VCSVendor vcsVendor, String accountId) {
        VendorApiService service = getMatchServiceByVendor(vcsVendor);
        return service.getOAuthAccessTokenByAccountId(accountId);
    }
}
