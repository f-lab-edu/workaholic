package com.project.workaholic.deploy.repository.custom;

import com.project.workaholic.deploy.model.entity.KubeNamespace;
import com.project.workaholic.deploy.model.entity.QKubeNamespace;
import com.querydsl.jpa.JPQLQueryFactory;

public class CustomKubeNamespaceRepositoryImpl implements CustomKubeNamespaceRepository{
    private final JPQLQueryFactory jpqlQueryFactory;
    QKubeNamespace qKubeNamespace = QKubeNamespace.kubeNamespace;

    public CustomKubeNamespaceRepositoryImpl(JPQLQueryFactory jpqlQueryFactory) {
        this.jpqlQueryFactory = jpqlQueryFactory;
    }
    @Override
    public KubeNamespace findNamespaceByAccountId(String accountId) {
        return jpqlQueryFactory.selectFrom(qKubeNamespace)
                .where(qKubeNamespace.accountId.eq(accountId))
                .fetchOne();
    }
}
