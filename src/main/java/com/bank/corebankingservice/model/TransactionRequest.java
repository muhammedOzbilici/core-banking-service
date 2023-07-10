package com.bank.corebankingservice.model;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountId;
    private double amount;
    private String currency;
    private String direction;
    private String description;
}
