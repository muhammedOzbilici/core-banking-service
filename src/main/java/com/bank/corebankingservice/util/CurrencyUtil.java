package com.bank.corebankingservice.util;

import com.bank.corebankingservice.exception.InvalidCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CurrencyUtil {

    @Value("allowed-currencies")
    private List<String> allowedCurrencies;

    public void checkCurrencyIsValid(List<String> currencies) {
        for (String currency : currencies) {
            if (!allowedCurrencies.contains(currency)) {
                String errorMessage = "currency is not allowed or wrong currency format, currency:" + currency;
                log.error(errorMessage);
                throw new InvalidCurrencyException(errorMessage);
            }
        }
    }
}
