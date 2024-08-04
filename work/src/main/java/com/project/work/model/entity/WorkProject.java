package com.project.work.model.entity;

import com.project.work.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Setter
    @Column(name = "COMMIT_URL")
    private String commitUrl;

    @Setter
    @Column(name = "BRANCH_URL")
    private String branchUrl;

    @Setter
    @Column(name = "CLONE_URL")
    private String cloneUrl;

    @Column(name = "VENDOR")
    @Enumerated(EnumType.STRING)
    private VCSVendor vendor;

    public WorkProject(String name, String commitUrl, String branchUrl, String cloneUrl, VCSVendor vendor) {
        this.id = UUID.randomUUID();;
        this.name = name;
        this.commitUrl = commitUrl;
        this.branchUrl = branchUrl;
        this.cloneUrl = cloneUrl;
        this.vendor = vendor;
    }

    public WorkProject(String name, VCSVendor vendor) {
        this.name = name;
        this.vendor = vendor;
    }
}
