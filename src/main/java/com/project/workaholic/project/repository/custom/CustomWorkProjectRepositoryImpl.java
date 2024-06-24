package com.project.workaholic.project.repository.custom;

import com.project.workaholic.project.model.entity.QWorkProject;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomWorkProjectRepositoryImpl implements CustomWorkProjectRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QWorkProject qWorkProject = QWorkProject.workProject;

    public CustomWorkProjectRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public boolean duplicateCheckByProjectName(String projectName) {
        return jpaQueryFactory.select(qWorkProject.count())
                .from(qWorkProject)
                .where(qWorkProject.name.eq(projectName))
                .fetchOne() != null;
    }
}
