package com.bank.corebankingservice.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class Account implements Serializable {
    private Long id;
    private Long customerId;
    private String country;
    private Set<Balance> balances;
    private int version;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
