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
@Table( name = "ACCOUNT")
public class OAuthAccount {
    @Id
    @Column(name="ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "MAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    public OAuthAccount(Long id, String password, String email, String name, LocalDateTime createAt) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.createAt = createAt;
    }
}
