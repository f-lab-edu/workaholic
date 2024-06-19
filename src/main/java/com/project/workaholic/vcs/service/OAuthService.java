package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import com.project.workaholic.vcs.vendor.github.service.GithubService;
import com.project.workaholic.vcs.vendor.gitlab.service.GitlabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GithubService githubService;
    private final GitlabService gitlabService;
    private final OAuthAccessTokenRepository tokenRepository;

    public RedirectView importVCS(RedirectAttributes redirectAttributes, VCSVendor vendor) {
        return switch (vendor) {
            case GITHUB -> githubService.requestCode(redirectAttributes);
            case GITLAB -> gitlabService.requestCode(redirectAttributes);
        };
    }

    public void registerToken(String accountId, String token, VCSVendor vendor) {
        OAuthAccessToken oAuthAccessToken = OAuthAccessToken.builder()
                .accountId(accountId)
                .type(vendor)
                .token(token)
                .build();
        tokenRepository.save(oAuthAccessToken);
    }

    public OAuthAccessToken findAccessTokenByAccountId(String accountId) {
        return tokenRepository.findGithubByAccountId(accountId).orElse(null);
    }

    public void updateAccessToken(OAuthAccessToken updatedToken, String accessToken) {
        updatedToken.setToken(accessToken);
        tokenRepository.save(updatedToken);
    }
}
