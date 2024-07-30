package com.project.oauth.model.entity;

import com.project.oauth.model.enumeration.VCSVendor;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Id
    private String id;

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

    public OAuthAccessToken(String accountId, VCSVendor type, String token) {
        this.id = type + "_" + accountId;
        this.accountId = accountId;
        this.type = type;
        this.token = token;
    }
}
