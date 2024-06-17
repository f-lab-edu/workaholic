package com.project.workaholic.vcs.service;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthGithubService githubService;
    private final OAuthAccessTokenRepository tokenRepository;

    public RedirectView importVCS(RedirectAttributes redirectAttributes, VCSVendor vendor) {
        switch (vendor) {
            case GITHUB :
                    return githubService.requestCode(redirectAttributes);
            default:
                throw new CustomException(StatusCode.INVALID_VCS_VENDOR);
        }
    }

    public String registerToken(String accountId, String token, VCSVendor vendor) {
        OAuthAccessToken oAuthAccessToken = OAuthAccessToken.builder()
                .accountId(accountId)
                .type(vendor)
                .token(token)
                .build();
        oAuthAccessToken = tokenRepository.save(oAuthAccessToken);
        return oAuthAccessToken.getToken();
    }

    public String getAccessToken(String accountId) {
        OAuthAccessToken token = tokenRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_OAUTH_ACCESS_TOKEN));

        return token.getToken();
    }
}
