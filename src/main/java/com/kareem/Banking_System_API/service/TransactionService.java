package com.kareem.Banking_System_API.service;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.Transaction;
import com.kareem.Banking_System_API.model.TransactionType;
import com.kareem.Banking_System_API.model.User;
import com.kareem.Banking_System_API.repository.AccountRepository;
import com.kareem.Banking_System_API.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private double DAILY_TRANSACTION_LIMIT = 10000.0;

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

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        double totalToday = getTotalWithdrawnOrTransferredToday(bankAccount);
        if (totalToday + amount > DAILY_TRANSACTION_LIMIT) {
            throw new RuntimeException("Exceeded daily transaction limit");
        }
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

        double totalToday = getTotalWithdrawnOrTransferredToday(fromAccount);

        if (totalToday + amount > DAILY_TRANSACTION_LIMIT) {
            throw new RuntimeException("Exceeded daily transaction limit");
        }

        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have the right to transfer from an account that is not yours");
        }

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("The balance is insufficient");
        }
        fromAccount.setBalance((fromAccount.getBalance() - amount) - setTax(amount));
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

    private double setTax(double amount) {
        double tax = 0;
        if (amount > 70000) {
            tax = 20.0;
        } else {
            tax = 0.5;
        }
        return tax;

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

    public double getTotalWithdrawnOrTransferredToday(BankAccount account) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        return transactionRepository.findByBankAccountAndTypeInAndTimestampBetween(
                        account,
                        List.of(TransactionType.WITHDRAW, TransactionType.OUT),
                        startOfDay,
                        endOfDay
                ).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();


    }


    public BankAccount getAccountIfAuthorized(Long accountId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have access to this account");
        }
        return account;
    }

    public BankAccount getAccountById(Long id, User user) {

        BankAccount account = bankAccountRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Account not found")
        );
        if (!account.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to access this account");
        }
        return account;
    }
}
