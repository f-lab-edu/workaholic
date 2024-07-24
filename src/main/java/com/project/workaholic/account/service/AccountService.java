package com.project.workaholic.account.service;

import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.repository.AccountRepository;
import com.project.workaholic.config.encoder.PasswordEncoder;
import com.project.workaholic.config.exception.type.DuplicateAccountException;
import com.project.workaholic.config.exception.type.InvalidAccountException;
import com.project.workaholic.config.exception.type.NotFoundAccountException;
import com.project.workaholic.deploy.service.DeployService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final DeployService deployService;

    @Transactional
    public void registerAccount(Account account) {
        if( accountRepository.existsById(account.getId()))
            throw new DuplicateAccountException();
        account.setPassword(passwordEncoder.encrypt(account.getId(), account.getPassword()));
        account = accountRepository.save(account);
        deployService.createNamespaceByAccountId(account.getId());
    }

    public Account verifyAccount(Account account) {
        return accountRepository.findById(account.getId())
                .filter( target -> target.getPassword().equals(passwordEncoder.encrypt(account.getId(), account.getPassword())))
                .orElseThrow(InvalidAccountException::new);
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(NotFoundAccountException::new);
    }

    public boolean checkExistAccountById(String accountId) {
        return accountRepository.existsByAccountId(accountId);
    }
}
