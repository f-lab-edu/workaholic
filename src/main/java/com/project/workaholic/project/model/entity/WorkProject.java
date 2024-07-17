package com.project.workaholic.project.model.entity;


import com.project.workaholic.config.exception.type.NonSupportedAlgorithmException;
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

    @Column(name = "REPOSITORY_NAME")
    private String repositoryName;

    @Column(name = "COMMIT_URL")
    private String commitUrl;

    @Column(name = "BRANCH_URL")
    private String branchUrl;

    @Column(name = "CLONE_URL")
    private String cloneUrl;

    @Column(name = "VENDOR")
    @Enumerated(EnumType.STRING)
    private VCSVendor vendor;

    @Column(name = "OWNER_ID")
    private String owner;

    public WorkProject(String name, String repositoryName, String commitUrl, String branchUrl, String cloneUrl, VCSVendor vendor, String owner) {
        this.name = name;
        this.repositoryName = repositoryName;
        this.commitUrl = commitUrl;
        this.branchUrl = branchUrl;
        this.cloneUrl = cloneUrl;
        this.vendor = vendor;
        this.owner = owner;
        this.id = hashingId(vendor.name(), repositoryName, owner);
    }

    private String hashingId(String vendor, String repositoryName, String owner) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash;
            hash = digest.digest(String.format(vendor + ":" + repositoryName + ":" + owner).getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new NonSupportedAlgorithmException("SHA-256");
        }
    }
}
