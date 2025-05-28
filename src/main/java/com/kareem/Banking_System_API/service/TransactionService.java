package com.kareem.Banking_System_API.service;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.Transaction;
import com.kareem.Banking_System_API.model.TransactionType;
import com.kareem.Banking_System_API.repository.AccountRepository;
import com.kareem.Banking_System_API.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public void deposit(Long accountId , double amount){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Can't Access");
        }
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .timestamp(LocalDateTime.now())
                .bankAccount(account).build();

        transactionRepository.save(transaction);
        bankAccountRepository.save(account);
    }

    public void withdraw (Long accountId , double amount){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Can't Access");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .timestamp(LocalDateTime.now())
                .bankAccount(account)
                .build();

        transactionRepository.save(transaction);
        bankAccountRepository.save(account);
    }

}
