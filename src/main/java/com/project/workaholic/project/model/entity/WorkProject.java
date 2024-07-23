package com.project.workaholic.project.model.entity;


import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id
    @Column(name = "ID")
    private UUID id;

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
        this.id = UUID.randomUUID();
        this.name = name;
        this.repositoryName = repositoryName;
        this.commitUrl = commitUrl;
        this.branchUrl = branchUrl;
        this.cloneUrl = cloneUrl;
        this.vendor = vendor;
        this.owner = owner;
    }
}
