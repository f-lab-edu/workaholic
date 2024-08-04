package com.project.deploy.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "NAMESPACE")
public class KubeNamespace {
    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    public KubeNamespace(String projectName) {
        this.id = UUID.randomUUID();
        this.projectName = projectName;
    }
}
