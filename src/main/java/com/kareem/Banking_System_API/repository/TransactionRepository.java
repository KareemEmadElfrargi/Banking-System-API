package com.kareem.Banking_System_API.repository;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.Transaction;
import com.kareem.Banking_System_API.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBankAccountId(Long accountId);

    List<Transaction> findByBankAccountAndTypeInAndTimestampBetween(BankAccount account, List<TransactionType> types, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<Transaction> findTop5ByBankAccountOrderByTimestampDesc(BankAccount account);

}
