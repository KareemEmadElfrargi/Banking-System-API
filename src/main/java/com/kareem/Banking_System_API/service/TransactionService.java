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
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private double tax = 0;

    public void deposit(Long accountId, double amount) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

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

    public void withdraw(Long accountId, double amount) {
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

    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have the right to transfer from an account that is not yours");
        }

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("The balance is insufficient");
        }
        tax = amount * 0.05;
        fromAccount.setBalance(fromAccount.getBalance() - amount - tax);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transaction outTransaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.OUT)
                .timestamp(LocalDateTime.now())
                .bankAccount(fromAccount)
                .build();

        Transaction inTransaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.IN)
                .timestamp(LocalDateTime.now())
                .bankAccount(toAccount)
                .build();

        transactionRepository.save(outTransaction);
        transactionRepository.save(inTransaction);

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to access this account’s transactions");
        }

        return transactionRepository.findByBankAccountId(accountId);
    }

}
