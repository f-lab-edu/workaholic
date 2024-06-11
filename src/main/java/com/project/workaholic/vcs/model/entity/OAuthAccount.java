package com.project.workaholic.vcs.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table( name = "account")
public class OAuthAccount {
    @Id
    @Column(name="account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String profileImg;
    private String username;
    private String nickname;
    private String role;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    public OAuthAccount(Long id, String email, String profileImg, String username, String nickname, String role, LocalDateTime createAt) {
        this.id = id;
        this.email = email;
        this.profileImg = profileImg;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.createAt = createAt;
    }
}
