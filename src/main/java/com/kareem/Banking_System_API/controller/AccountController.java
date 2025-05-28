package com.kareem.Banking_System_API.controller;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.User;
import com.kareem.Banking_System_API.service.AccountService;
import com.kareem.Banking_System_API.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<BankAccount>> getAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        User user = ((UserPrincipal) userDetails).getUser();
        List<BankAccount> accounts = accountService.getAccountsByUser(user);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody BankAccount account) {
        User user = ((UserPrincipal) userDetails).getUser();
        account.setUser(user);

        BankAccount savedAccount = accountService.createAccount(account);
        return ResponseEntity.ok(savedAccount);
    }

}
