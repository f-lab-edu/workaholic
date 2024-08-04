package com.project.auth.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "AUTHENTICATION")
public class AuthenticationToken {
    @Id @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "TOKEN")
    private String token;

    public AuthenticationToken(UUID id, String token) {
        this.id = id;
        this.token = token;
    }
}
