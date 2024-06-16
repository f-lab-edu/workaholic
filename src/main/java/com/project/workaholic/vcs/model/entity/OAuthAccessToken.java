package com.project.workaholic.vcs.model.entity;

import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OAUTH_TOKEN")
public class OAuthAccessToken {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private VCSVendor type;

    @Column(name = "DATE")
    @LastModifiedDate
    private LocalDateTime issueDate;

    @Builder
    public OAuthAccessToken(Long id, Account account, VCSVendor type, LocalDateTime issueDate) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.issueDate = issueDate;
    }
}
