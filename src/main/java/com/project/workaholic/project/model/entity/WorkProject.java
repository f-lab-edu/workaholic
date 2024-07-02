package com.project.workaholic.project.model.entity;


import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REPOSITORY")
    private String repository;

    @Column(name = "REPO_NAME")
    private String repositoryName;

    @Column(name = "VENDOR")
    @Enumerated(EnumType.STRING)
    private VCSVendor vendor;

    @Column(name = "OWNER_ID")
    private String owner;

    public WorkProject(String name, String repository, String repositoryName, String owner, VCSVendor vendor) {
        this.name = name;
        this.repository = repository;
        this.repositoryName = repositoryName;
        this.vendor = vendor;
        this.owner = owner;
        this.id = hashingId(repository, owner);
    }

    private String hashingId(String repository, String owner) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash;
            hash = digest.digest(String.format(repository + ":" + owner).getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(StatusCode.ERROR);
        }
    }
}
