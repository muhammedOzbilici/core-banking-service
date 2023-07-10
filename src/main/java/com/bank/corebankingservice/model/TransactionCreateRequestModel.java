package com.bank.corebankingservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountCreateRequestModel {
    private Long customerId;
    private String country;
    private List<String> currencies;
}
