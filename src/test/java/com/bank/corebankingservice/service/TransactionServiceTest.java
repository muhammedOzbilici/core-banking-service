package com.bank.corebankingservice.service;

import com.bank.corebankingservice.entity.Direction;
import com.bank.corebankingservice.exception.AccountNotFoundException;
import com.bank.corebankingservice.exception.InsufficientFundException;
import com.bank.corebankingservice.exception.InvalidAmountException;
import com.bank.corebankingservice.mapper.BalanceMapper;
import com.bank.corebankingservice.mapper.TransactionMapper;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.model.AccountCreateResponseModel;
import com.bank.corebankingservice.model.TransactionCreateRequestModel;
import com.bank.corebankingservice.model.TransactionCreateResponseModel;
import com.bank.corebankingservice.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionMapper, balanceMapper, accountService);
    }

    @ParameterizedTest(name = "for currency {0}")
    @ValueSource(strings = {"EUR,USD,GBP"})
    void createTransactionDeposit(Currency currency) {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        AccountCreateResponseModel response = accountService.createAccount(model);
        assertNotNull(response);

        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("1234.34"))
                .currency(Currency.getInstance("EUR"))
                .direction(Direction.IN)
                .build();

        TransactionCreateResponseModel createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        Assertions.assertEquals(createdTransaction.getAmount().compareTo(new BigDecimal("1000.00")), 0);
        assertEquals(createdTransaction.getAccountId(), response.getAccountId());
        Assertions.assertEquals(createdTransaction.getCurrency(), currency);
        Assertions.assertEquals(createdTransaction.getDirection(), Direction.IN);
    }

    @ParameterizedTest(name = "for currency {0}")
    @MethodSource("com.bank.corebankingservice.util.TestUtil#getAllowedCurrencies")
    void createTransactionWithdraw(Currency currency) {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);
        AccountCreateResponseModel response = accountService.createAccount(model);
        assertNotNull(response);

        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("1234.34"))
                .currency(Currency.getInstance("EUR"))
                .direction(Direction.IN)
                .build();

        TransactionCreateResponseModel createdTransaction = transactionService.createTransaction(transaction);
        assertNotNull(createdTransaction);

        transaction.setAmount(new BigDecimal("10.00"));
        transaction.setDirection(Direction.OUT);
        createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertNotNull(createdTransaction.getAmount());
        Assertions.assertEquals(createdTransaction.getAmount().compareTo(new BigDecimal("90.00")), 0);
        assertEquals(createdTransaction.getAccountId(), response.getAccountId());
        Assertions.assertEquals(createdTransaction.getCurrency(), currency);
        Assertions.assertEquals(createdTransaction.getDirection(), Direction.OUT);
    }

    @ParameterizedTest(name = "for currency {0} and direction {1}")
    @MethodSource("com.bank.corebankingservice.util.TestUtil#currencyAndDirectionCombinator")
    void createTransactionThrowsAccountNotFoundException(Currency currency, Direction direction) {
        long generatedAccountId = TestUtil.generateRandomId();

        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(generatedAccountId)
                .amount(new BigDecimal("1000.00"))
                .currency(currency)
                .direction(direction)
                .build();

        assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.createTransaction(transaction)
        );

    }

    @ParameterizedTest(name = "for currency {0}")
    @MethodSource("com.bank.corebankingservice.util.TestUtil#getAllowedCurrencies")
    void createTransactionThrowsInsufficientFundException(Currency currency) {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of(currency.getCurrencyCode()));
        model.setCustomerId(generatedCustomerId);
        AccountCreateResponseModel response = accountService.createAccount(model);
        assertNotNull(response);

        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("1234.34"))
                .currency(currency)
                .direction(Direction.OUT)
                .build();

        assertThrows(
                InsufficientFundException.class,
                () -> transactionService.createTransaction(transaction)
        );

    }

    @ParameterizedTest(name = "for currency {0} and direction {1}")
    @MethodSource("com.bank.corebankingservice.util.TestUtil#currencyAndDirectionCombinator")
    void createTransactionThrowsInvalidAmountException(Currency currency, Direction direction) {
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of(currency.getCurrencyCode()));
        model.setCustomerId(generatedCustomerId);
        AccountCreateResponseModel response = accountService.createAccount(model);
        assertNotNull(response);

        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("10000.00").negate())
                .currency(currency)
                .direction(direction)
                .build();

        assertThrows(
                InvalidAmountException.class,
                () -> transactionService.createTransaction(transaction)
        );
    }

}