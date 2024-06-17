package com.project.workaholic.account.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String id;

    @NotBlank
    @Column(name = "TOKEN")
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    @Setter
    @Column(name = "LOGOUT")
    private boolean logout = false;

    @Builder
    public RefreshToken(String id, String token, Account account, boolean logout) {
        this.id = id;
        this.token = token;
        this.account = account;
        this.logout = logout;
    }

    public RefreshToken updateToken(String refreshToken) {
        this.token = refreshToken;
        return this;
    }
}
