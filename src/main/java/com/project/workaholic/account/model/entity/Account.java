package com.project.workaholic.account.model.entity;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ACCOUNT")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @Column(name="ACCOUNT_ID")
    private String id;

    @Setter
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    public Account(String id, String password, String name, LocalDateTime createAt) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.createAt = createAt;
    }
}
