package com.bank.corebankingservice.service;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.entity.Balance;
import com.bank.corebankingservice.entity.Transaction;
import com.bank.corebankingservice.exception.InsufficientFundException;
import com.bank.corebankingservice.exception.InvalidAmountException;
import com.bank.corebankingservice.exception.InvalidDirectionException;
import com.bank.corebankingservice.mapper.BalanceMapper;
import com.bank.corebankingservice.mapper.TransactionMapper;
import com.bank.corebankingservice.entity.Direction;
import com.bank.corebankingservice.model.TransactionCreateRequestModel;
import com.bank.corebankingservice.model.TransactionCreateResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransactionCreateResponseModel createTransaction(TransactionCreateRequestModel request) {
        checkAmountIsValid(request.getAmount());

        checkDirectionIsValid(request.getDirection());

        Account foundedAccount = accountService.getAccountWithId(request.getAccountId());

        int balanceComparison = balanceMapper.getBalance(
                request.getAccountId(),
                request.getCurrency()
        ).getAmount().subtract(request.getAmount()).compareTo(BigDecimal.ZERO);

        if (Direction.OUT.equals(request.getDirection())) {
            if (balanceComparison < 0) {
                throw new InsufficientFundException("Insufficient Fund");
            } else {
                balanceMapper.decreaseBalance(
                        foundedAccount.getId(),
                        request.getCurrency(),
                        request.getAmount()
                );
            }
        } else if (Direction.IN.equals(request.getDirection())) {
            balanceMapper.increaseBalance(
                    foundedAccount.getId(),
                    request.getCurrency(),
                    request.getAmount()
            );
        }

        Balance balance = balanceMapper.getBalance(foundedAccount.getId(), request.getCurrency());
        Transaction newTransaction = Transaction.builder()
                .accountId(request.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .direction(request.getDirection().getValue())
                .description(request.getDescription())
                .balance(balance)
                .createdDate(LocalDateTime.now()).
                build();

        long createdTransactionId = transactionMapper.createTransaction(newTransaction);
        log.info("Transaction successfully created");
        return TransactionCreateResponseModel.builder()
                .accountId(newTransaction.getAccountId())
                .transactionId(createdTransactionId)
                .amount(newTransaction.getAmount())
                .currency(newTransaction.getCurrency())
                .direction(request.getDirection())
                .description(request.getDescription())
                .finalAmount(balance.getAmount())
                .build();
    }

    private static void checkAmountIsValid(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Invalid Amount, amount:" + amount);
        }
    }

    private static void checkDirectionIsValid(Direction direction) {
        if (!List.of(Direction.IN, Direction.OUT).contains(direction)) {
            throw new InvalidDirectionException("Invalid direction, direction:" + direction);
        }
    }
}
