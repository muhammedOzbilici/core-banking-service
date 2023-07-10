package com.bank.corebankingservice.controller;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.model.AccountCreateRequestModel;
import com.bank.corebankingservice.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loadContext() {
        assertNotNull(mvc);
    }

    @Test
    public void should_create_account() throws Exception {
        //given
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        //then
        mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(model.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").value(0L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").value(0L));
    }

    @Test
    public void should_get_account() throws Exception {
        //given
        long generatedCustomerId = TestUtil.generateRandomId();
        AccountCreateRequestModel model = new AccountCreateRequestModel();
        model.setCountry("Germany");
        model.setCurrencies(List.of("EUR", "USD"));
        model.setCustomerId(generatedCustomerId);

        //when
        MvcResult result = mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model))
                )
                .andExpect(status().isCreated()).andReturn();
        Account createdAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);

        //then
        mvc.perform(get("/api/v1/accounts/" + createdAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(createdAccount.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").value(0L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").value(0L));
    }
}
