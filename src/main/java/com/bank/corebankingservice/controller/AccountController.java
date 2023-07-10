package com.bank.corebankingservice.controller;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.model.AccountCreateResponseModel;
import com.bank.corebankingservice.model.AccountDto;
import com.bank.corebankingservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@ControllerAdvice
@CrossOrigin
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreateResponseModel> createAccount(@RequestBody AccountCreateRequestModel request) {
        AccountCreateResponseModel response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccountWithId(accountId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AccountDto.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balances(account.getBalances())
                .build());
    }

}
