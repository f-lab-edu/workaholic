package com.project.workaholic.vcs.model.entity;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OAUTH_TOKEN")
@EntityListeners(AuditingEntityListener.class)
public class OAuthAccessToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private VCSVendor type;

    @Setter
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "DATE")
    @LastModifiedDate
    private LocalDateTime issueDate;

    @Builder
    public OAuthAccessToken(Long id, String accountId, VCSVendor type, String token, LocalDateTime issueDate) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.token = token;
        this.issueDate = issueDate;
    }
}
