package com.project.deploy.repository.custom;

import com.project.deploy.model.entity.KubeNamespace;

public interface CustomKubeNamespaceRepository {
    KubeNamespace findNamespaceByAccountId(String accountId);
}
