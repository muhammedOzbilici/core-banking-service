package com.bank.corebankingservice.controller;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.entity.Direction;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.model.AccountCreateResponseModel;
import com.bank.corebankingservice.model.TransactionCreateRequestModel;
import com.bank.corebankingservice.service.AccountService;
import com.bank.corebankingservice.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loadContext() {
        assertNotNull(mvc);
        assertNotNull(objectMapper);
    }

    @Test
    void should_create_transaction() throws Exception {
        //given
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        AccountCreateResponseModel response = accountService.createAccount(model);

        //when
        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(new BigDecimal("1234.34"))
                .currency(Currency.getInstance("EUR"))
                .description("test description")
                .direction(Direction.IN)
                .build();

        //then
        mvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void should_return_transactions() throws Exception {
        //given
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        MvcResult result = mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model))
                )
                .andExpect(status().isCreated()).andReturn();
        objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);

        AccountCreateResponseModel response = accountService.createAccount(model);

        //when
        Currency curreny = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("1234.34");
        Direction direction = Direction.IN;
        TransactionCreateRequestModel transaction = TransactionCreateRequestModel.builder()
                .accountId(response.getAccountId())
                .amount(amount)
                .currency(curreny)
                .description("test description")
                .direction(direction)
                .build();

        mvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                )
                .andExpect(status().isCreated());

        //then
        mvc.perform(get("/api/v1/transactions/" + response.getAccountId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].accountId").value(response.getAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].currency").value(curreny))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].direction").value(direction));
    }

}
