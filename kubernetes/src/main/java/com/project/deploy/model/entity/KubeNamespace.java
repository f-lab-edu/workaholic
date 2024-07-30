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

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    public KubeNamespace(String accountId) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
    }
}
