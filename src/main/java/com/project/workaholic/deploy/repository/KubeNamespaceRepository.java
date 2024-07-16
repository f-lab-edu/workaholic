package com.project.workaholic.deploy.repository;

import com.project.workaholic.deploy.model.entity.KubeNamespace;
import com.project.workaholic.deploy.repository.custom.CustomKubeNamespaceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KubeNamespaceRepository extends JpaRepository<KubeNamespace, UUID>, CustomKubeNamespaceRepository {
}
