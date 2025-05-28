package com.kareem.Banking_System_API.repository;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUser(User user);
}
