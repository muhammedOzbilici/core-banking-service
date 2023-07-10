package com.bank.corebankingservice.model;

import com.bank.corebankingservice.entity.Balance;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AccountDto {
    private Long id;
    private Long customerId;
    private Set<Balance> balances;
}
