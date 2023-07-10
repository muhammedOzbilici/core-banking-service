package com.bank.corebankingservice.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
public class Balance implements Serializable {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime createdDate;
}
