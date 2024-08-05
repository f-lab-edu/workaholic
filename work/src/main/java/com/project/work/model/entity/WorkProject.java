package com.project.work.model.entity;

import com.project.work.model.enumeration.VCSVendor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id
    @Column(name = "ID", unique = true, updatable = false, nullable = false)
    private String id;

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

    public WorkProject(String id, VCSVendor vendor) {
        this.id = id;
        this.vendor = vendor;
    }
}
