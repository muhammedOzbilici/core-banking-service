package com.bank.corebankingservice.service;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.entity.Balance;
import com.bank.corebankingservice.entity.Transaction;
import com.bank.corebankingservice.exception.AccountExistsException;
import com.bank.corebankingservice.exception.AccountNotFoundException;
import com.bank.corebankingservice.mapper.AccountMapper;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.model.AccountCreateResponseModel;
import com.bank.corebankingservice.util.CurrencyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;
    private final CurrencyUtil currencyUtil;

    public Account getAccountWithId(Long id) {
        Account foundedAccount = accountMapper.getAccountById(id);
        if (foundedAccount == null)
            throw new AccountNotFoundException("couldn't find account with this id:" + id);
        return foundedAccount;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AccountCreateResponseModel createAccount(AccountCreateRequestModel request) {
        try {
            if (accountMapper.getAccountByCustomerId(request.getCustomerId()) == null) {
                String errorMessage = "Account exists with this customer id:" + request.getCustomerId();
                log.error(errorMessage);
                throw new AccountExistsException(errorMessage);
            }

            currencyUtil.checkCurrencyIsValid(request.getCurrencies());

            Set<Balance> balances = new HashSet<>();
            for (String currency : request.getCurrencies()) {
                balances.add(Balance.builder()
                        .amount(BigDecimal.ZERO)
                        .currency(Currency.getInstance(currency))
                        .createdDate(LocalDateTime.now())
                        .build());
            }

            long createdAccountId = accountMapper.createAccount(Account.builder()
                    .customerId(request.getCustomerId())
                    .balances(balances)
                    .createdDate(LocalDateTime.now())
                    .build());

            log.info("Account successfully created, accountId:{}", createdAccountId);

            return AccountCreateResponseModel.builder()
                    .accountId(createdAccountId)
                    .balances(balances)
                    .build();
        } catch (Exception e) {
            log.error("Error happened during account creation", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsWithAccountId(Long accountId, Short page, Short size) {
        if (accountMapper.getAccountById(accountId) == null) {
            throw new AccountNotFoundException("couldn't find account with this accountId:" + accountId);
        }
        log.info("Returning transactions of account with id:{}", accountId);
        return accountMapper.getTransactions(accountId, page * size, size);
    }

    @Transactional(readOnly = true)
    public Account getAccountWithCustomerId(Long customerId) {
        Account foundedAccount = accountMapper.getAccountByCustomerId(customerId);
        if (foundedAccount == null)
            throw new AccountNotFoundException("couldn't find account with this customerId:" + customerId);
        return foundedAccount;
    }
}
