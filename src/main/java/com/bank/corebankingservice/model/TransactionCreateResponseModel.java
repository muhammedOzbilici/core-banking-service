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
public class TransactionCreateRequestModel {
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    private String Description;
}
