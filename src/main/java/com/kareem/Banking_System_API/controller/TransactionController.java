package com.kareem.Banking_System_API.controller;

import com.kareem.Banking_System_API.dto.TransactionRequest;
import com.kareem.Banking_System_API.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok("Money withdraw , amount : + amount");
    }
}

