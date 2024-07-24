package com.project.workaholic.deploy.repository.custom;

import com.project.workaholic.deploy.model.entity.KubeNamespace;

public interface CustomKubeNamespaceRepository {
    KubeNamespace findNamespaceByAccountId(String accountId);
}
