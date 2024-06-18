package com.project.workaholic.vcs.repository.custom;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.entity.QOAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.querydsl.core.types.dsl.Expressions;
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
}
