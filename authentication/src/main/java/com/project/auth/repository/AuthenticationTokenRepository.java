package com.project.auth.repository;

import com.project.auth.model.entity.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, UUID> {
}
