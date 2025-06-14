package com.kareem.Banking_System_API.repository;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUser(User user);
    @Query("SELECT SUM(a.balance) FROM BankAccount a WHERE a.user = :user")
    Double getTotalBalanceByUser(@Param("user") User user);
}
