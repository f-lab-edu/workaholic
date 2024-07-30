package com.project.oauth.repository;

import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.repository.custom.CustomOAuthAccessTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken, String>, CustomOAuthAccessTokenRepository {

}
