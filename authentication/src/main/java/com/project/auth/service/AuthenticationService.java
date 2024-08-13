package com.project.auth.service;

import com.project.auth.repository.AuthenticationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationTokenRepository tokenRepository;

    public AuthenticationService(AuthenticationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
