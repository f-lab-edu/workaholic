package com.project.deploy.repository.custom;


import com.project.deploy.model.entity.KubeNamespace;
import com.project.deploy.model.entity.QKubeNamespace;
import com.querydsl.jpa.JPQLQueryFactory;

public class CustomKubeNamespaceRepositoryImpl implements CustomKubeNamespaceRepository{
    private final JPQLQueryFactory jpqlQueryFactory;
    QKubeNamespace qKubeNamespace = QKubeNamespace.kubeNamespace;

    public CustomKubeNamespaceRepositoryImpl(JPQLQueryFactory jpqlQueryFactory) {
        this.jpqlQueryFactory = jpqlQueryFactory;
    }

    @Override
    public KubeNamespace findNamespaceByProjectName(String projectName) {
        return jpqlQueryFactory.selectFrom(qKubeNamespace)
                .where(qKubeNamespace.projectName.eq(projectName))
                .fetchOne();
    }
}
