package com.kareem.Banking_System_API.controller;

import com.kareem.Banking_System_API.dto.TransactionRequest;
import com.kareem.Banking_System_API.dto.TransferRequest;
import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.Transaction;
import com.kareem.Banking_System_API.model.User;
import com.kareem.Banking_System_API.service.AccountService;
import com.kareem.Banking_System_API.service.TransactionService;
import com.kareem.Banking_System_API.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest request) {

        transactionService.deposit(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok("Money deposited , amount : " + request.getAmount());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest request) {
        transactionService.withdraw(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok("Money withdraw , amount : " + request.getAmount());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ResponseEntity.ok("Money transferred , amount : " + request.getAmount());
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{accountId}/withdrawn-today")
    public double getWithdrawnToday(@PathVariable Long accountId) {
        BankAccount account = transactionService.getAccountIfAuthorized(accountId);
        return transactionService.getTotalWithdrawnOrTransferredToday(account);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Double> getAccountBalance(@PathVariable Long accountId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = ((UserPrincipal) userDetails).getUser();
        BankAccount account = transactionService.getAccountById(accountId, user);
        return ResponseEntity.ok(account.getBalance());

    }

    @GetMapping("/last5/{accountId}")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long accountId
    ) {
        User user = ((UserPrincipal)userDetails).getUser();

        BankAccount account = transactionService.getAccountById(accountId,user);
        List<Transaction> transactionList = transactionService.getLast5Transactions(account);

        return ResponseEntity.ok(transactionList);

    }

}

