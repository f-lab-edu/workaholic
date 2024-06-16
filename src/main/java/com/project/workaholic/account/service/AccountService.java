package com.project.workaholic.account.service;

import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.repository.AccountRepository;
import com.project.workaholic.config.encoder.PasswordEncoder;
import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public void registerAccount(Account account) {
        if( accountRepository.existsById(account.getId()))
            throw new CustomException(StatusCode.EXISTS_ACCOUNT_ID);
        account.setPassword(passwordEncoder.encrypt(account.getId(), account.getPassword()));
        accountRepository.save(account);
    }

    public Account verifyAccount(Account account) {
        return accountRepository.findById(account.getId())
                .filter( target -> target.getPassword().equals(passwordEncoder.encrypt(account.getId(), account.getPassword())))
                .orElseThrow(() -> new CustomException(StatusCode.INVALID_ACCOUNT));
    }
}
