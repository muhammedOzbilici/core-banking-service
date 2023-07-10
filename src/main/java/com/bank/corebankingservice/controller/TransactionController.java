package com.bank.corebankingservice.controller;

import com.bank.corebankingservice.entity.Transaction;
import com.bank.corebankingservice.model.TransactionCreateRequestModel;
import com.bank.corebankingservice.model.TransactionCreateResponseModel;
import com.bank.corebankingservice.service.AccountService;
import com.bank.corebankingservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@ControllerAdvice
@CrossOrigin
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<TransactionCreateResponseModel> createTransaction(@RequestBody TransactionCreateRequestModel request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsWithAccountId(
            @PathVariable("accountId") Long accountId,
            @RequestParam(value = "page", defaultValue = "0") Short page,
            @RequestParam(value = "size", defaultValue = "10") Short size) {
        return ResponseEntity.ok(accountService.getTransactionsWithAccountId(accountId, page, size));
    }
}
