package com.project.workaholic.project.model.entity;


import com.project.workaholic.account.model.entity.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "NAME")
    private String name;

    @Column(name = "REPO_NAME")
    private String repositoryName;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account owner;

    @Builder
    public WorkProject(String name, String repositoryName, Account owner) {
        this.name = name;
        this.repositoryName = repositoryName;
        this.owner = owner;
    }
}
