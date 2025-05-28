package com.kareem.Banking_System_API.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountId;
    private Double amount;
}