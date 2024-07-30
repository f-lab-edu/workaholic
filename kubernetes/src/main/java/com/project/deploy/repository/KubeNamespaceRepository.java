package com.project.deploy.repository;

import com.project.deploy.model.entity.KubeNamespace;
import com.project.deploy.repository.custom.CustomKubeNamespaceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KubeNamespaceRepository extends JpaRepository<KubeNamespace, UUID>, CustomKubeNamespaceRepository {
}
