package com.bank.corebankingservice.model;

import com.bank.corebankingservice.entity.Balance;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class AccountCreateResponseModel {
    private Long accountId;
    private Long customerId;
    private Set<Balance> balances;
}
