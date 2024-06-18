package com.project.workaholic.vcs.repository;

import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.repository.custom.CustomOAuthAccessTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken, Long>, CustomOAuthAccessTokenRepository {

}
