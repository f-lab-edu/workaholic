package com.project.oauth.repository.custom;

import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.entity.QOAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomOAuthAccessTokenRepositoryImpl implements CustomOAuthAccessTokenRepository{
    private final JPAQueryFactory jpaQueryFactory;

    QOAuthAccessToken qOAuthAccessToken = QOAuthAccessToken.oAuthAccessToken;

    @Override
    public Optional<OAuthAccessToken> findGithubByAccountId(String accountId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(qOAuthAccessToken)
                        .where(qOAuthAccessToken.accountId.eq(accountId)
                                .and(qOAuthAccessToken.type.eq(VCSVendor.GITHUB)))
                        .fetchOne());
    }

    @Override
    public Optional<OAuthAccessToken> findGitlabByAccountId(String accountId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(qOAuthAccessToken)
                        .where(qOAuthAccessToken.accountId.eq(accountId)
                                .and(qOAuthAccessToken.type.eq(VCSVendor.GITLAB)))
                        .fetchOne());
    }
}
