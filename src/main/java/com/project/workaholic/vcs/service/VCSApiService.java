package com.project.workaholic.vcs.service;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class VCSApiService {
    private final VendorManager vendorManager;
    private final OAuthAccessTokenRepository tokenRepository;

    public VCSApiService(VendorManager vendorManager, OAuthAccessTokenRepository tokenRepository) {
        this.vendorManager = vendorManager;
        this.tokenRepository = tokenRepository;
    }

    private VendorApiService getMatchServiceByWorkProject(WorkProject workProject) {
        return vendorManager.getService(workProject.getVendor())
                .orElseThrow(() -> new CustomException(StatusCode.NON_SUPPORTED_VENDOR));
    }

    public VendorApiService getMatchServiceByVendor(VCSVendor vendor) {
        return vendorManager.getService(vendor)
                .orElseThrow(() -> new CustomException(StatusCode.NON_SUPPORTED_VENDOR));
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
