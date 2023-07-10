package com.bank.corebankingservice.service;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.entity.Direction;
import com.bank.corebankingservice.entity.Transaction;
import com.bank.corebankingservice.exception.AccountNotFoundException;
import com.bank.corebankingservice.exception.InvalidCurrencyException;
import com.bank.corebankingservice.mapper.AccountMapper;
import com.bank.corebankingservice.mapper.BalanceMapper;
import com.bank.corebankingservice.mapper.TransactionMapper;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.model.AccountCreateResponseModel;
import com.bank.corebankingservice.util.CurrencyUtil;
import com.bank.corebankingservice.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
    @MockBean
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CurrencyUtil currencyUtil;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountMapper, currencyUtil);
    }

    @Test
    void createAccount() throws InvalidCurrencyException {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        AccountCreateResponseModel response = accountService.createAccount(model);
        assertNotNull(response);
        Assertions.assertEquals(response.getCustomerId(), generatedCustomerId);
        Assertions.assertEquals(response.getBalances().size(), 2);
        Assertions.assertEquals(response.getBalances().stream().mapToDouble(i -> i.getAmount().doubleValue()).sum(), 0d);
    }

    @Test
    void getAccount() {
        long generatedCustomerId = TestUtil.generateRandomId();
        Account account = Account.builder().customerId(generatedCustomerId).country("Germany").build();

        accountMapper.createAccount(account);

        Account foundedAccount = accountService.getAccountWithCustomerId(generatedCustomerId);
        assertNotNull(foundedAccount);

        assertEquals(foundedAccount.getCustomerId(), generatedCustomerId);
        assertEquals(account.getCountry(), "Iran");
        assertNotEquals(foundedAccount.getId(), 0L);

        long anotherGeneratedId = TestUtil.generateRandomId();
        assertNotEquals(generatedCustomerId, anotherGeneratedId);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountWithCustomerId(anotherGeneratedId));
    }

    @Test
    void getTransactions() throws AccountNotFoundException {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        AccountCreateResponseModel response = accountService.createAccount(model);

        Transaction transaction = Transaction.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("1234.34"))
                .currency(Currency.getInstance("EUR"))
                .direction(Direction.IN.getValue())
                .createdDate(LocalDateTime.now()).
                build();

        transactionMapper.createTransaction(transaction);
        assertNotNull(transaction);
        assertNotEquals(transaction.getId(), 1L);
        assertEquals(transaction.getAmount().compareTo(new BigDecimal("1234.34")), 0);

        var transactions = accountService.getTransactionsWithAccountId(response.getAccountId(), (short) 0, (short) 2);
        assertNotEquals(transactions.size(), 0);
        assertEquals(transactions.size(), 1);
        Assertions.assertEquals(transactions.get(0).getAccountId(), response.getAccountId());
        Assertions.assertEquals(transactions.get(0).getAmount().compareTo(new BigDecimal("1234.34")), 0);
        Assertions.assertEquals(transactions.get(0).getCurrency(), Currency.getInstance("EUR"));
        Assertions.assertEquals(transactions.get(0).getDirection(), Direction.IN.getValue());

        long generatedAccountId = TestUtil.generateRandomId();
        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getTransactionsWithAccountId(generatedAccountId, (short) 0, (short) 2));
    }
}