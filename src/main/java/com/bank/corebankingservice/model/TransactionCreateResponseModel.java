package com.bank.corebankingservice.model;

import com.bank.corebankingservice.entity.Direction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@Builder
public class TransactionCreateResponseModel {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    private String description;
    private BigDecimal finalAmount;
}
