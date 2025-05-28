package com.kareem.Banking_System_API.service;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.User;
import com.kareem.Banking_System_API.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepo accountRepository;
    public List<BankAccount> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public BankAccount createAccount(BankAccount account) {
        return accountRepository.save(account);
    }

}
