package com.project.workaholic.vcs.service;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthAccessTokenRepository tokenRepository;

    public void registerToken(String accountId, String token, VCSVendor vendor) {
        OAuthAccessToken oAuthAccessToken = OAuthAccessToken.builder()
                .accountId(accountId)
                .type(vendor)
                .token(token)
                .build();
        tokenRepository.save(oAuthAccessToken);
    }

    public OAuthAccessToken findAccessTokenByAccountId(String accountId, VCSVendor vendor) {
        switch (vendor) {
            case GITHUB -> {
                return tokenRepository.findGithubByAccountId(accountId).orElse(null);
            }
            case GITLAB -> {
                return tokenRepository.findGitlabByAccountId(accountId).orElse(null);
            }
            default -> throw new CustomException(StatusCode.NON_SUPPORTED_VENDOR);
        }
    }

    public String importedRepositoryByAccountId(String accountId, String repo, String repoUrl) {
        //TODO Account에서 Import한 Repository 정보 저장이 필요함, 로그인할 경우 불러와야함
        return repoUrl;
    }

    public void checkOutBranch(String branch) {
        //TODO issue 를 생성 후 본 이슈에 맞는 branch 설정을 위한 서비스 필요
    }
}
