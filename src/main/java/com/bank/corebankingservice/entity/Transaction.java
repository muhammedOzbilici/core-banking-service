package com.bank.corebankingservice.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
public class Transaction implements Serializable {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
    private int direction;
    private String description;
    private Balance balance;
    private LocalDateTime createdDate;
}
