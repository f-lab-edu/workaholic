package com.project.workaholic.vcs.model.repository;

import com.project.workaholic.vcs.model.entity.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {
    Optional<OAuthAccount> findByEmail(String email);
}
