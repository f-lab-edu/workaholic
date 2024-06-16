package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.repository.OAuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthAccountService {
    private final OAuthAccountRepository oAuthAccountRepository;

    public void register(String token) {
    }
}
