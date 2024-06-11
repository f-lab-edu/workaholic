package com.project.workaholic.vcs.model;

import com.project.workaholic.vcs.model.entity.OAuthAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuthPrincipalDetails implements OAuth2User {
    private OAuthAccount account;
    private Map<String, Object> attributes = new HashMap<>();

    public OAuthPrincipalDetails(OAuthAccount account, Map<String, Object> attributes) {
        this.account = account;
        this.attributes = attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return "";
    }
}
