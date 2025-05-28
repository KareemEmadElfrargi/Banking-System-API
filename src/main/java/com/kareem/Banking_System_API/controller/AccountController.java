package com.kareem.Banking_System_API.controller;

import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.User;
import com.kareem.Banking_System_API.repository.UserRepository;
import com.kareem.Banking_System_API.service.AccountService;
import com.kareem.Banking_System_API.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<BankAccount>> getAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        User user = ((UserPrincipal) userDetails).getUser();
        List<BankAccount> accounts = accountService.getAccountsByUser(user);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/create")
    public ResponseEntity<BankAccount> createAccount(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody BankAccount account) {
        User user = ((UserPrincipal) userDetails).getUser();
        account.setUser(user);

        BankAccount savedAccount = accountService.createAccount(account);
        return ResponseEntity.ok(savedAccount);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BankAccount>> getMyAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user.getBankAccounts());
    }
}
