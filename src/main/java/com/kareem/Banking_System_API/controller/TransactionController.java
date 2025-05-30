package com.kareem.Banking_System_API.controller;

import com.kareem.Banking_System_API.dto.TransactionRequest;
import com.kareem.Banking_System_API.dto.TransferRequest;
import com.kareem.Banking_System_API.model.BankAccount;
import com.kareem.Banking_System_API.model.Transaction;
import com.kareem.Banking_System_API.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest request) {

        transactionService.deposit(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok("Money deposited , amount : " + request.getAmount());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest request) {
        transactionService.withdraw(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok("Money withdraw , amount : "+ request.getAmount());
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(),request.getToAccountId(),request.getAmount());
        return ResponseEntity.ok("Money transferred , amount : "+ request.getAmount());
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable Long accountId){
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{accountId}/withdrawn-today")
    public double getWithdrawnToday(@PathVariable Long accountId){
        BankAccount account = transactionService.getAccountIfAuthorized(accountId);
        return transactionService.getTotalWithdrawnToday(account);
    }

}

