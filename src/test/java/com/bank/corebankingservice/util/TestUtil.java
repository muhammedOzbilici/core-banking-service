package com.bank.corebankingservice.util;

import com.bank.corebankingservice.entity.Direction;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Value;

import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TestUtil {

    @Value("allowed-currencies")
    private List<String> allowedCurrencies;

    private static List<String> allowedCurrenciesStatic;

    @Value("${allowed-currencies}")
    public void setAllowedCurrenciesStatic(List<String> currencies) {
        TestUtil.allowedCurrenciesStatic = currencies;
    }

    public static long generateRandomId() {
        return new Random().nextLong();
    }

    static Stream<Arguments> currencyAndDirectionCombinator() {
        List<Arguments> args = new LinkedList<>();
        for (Currency currency : Currency.getAvailableCurrencies()) {
            for (Direction direction : Direction.values())
                args.add(Arguments.arguments(currency, direction));
        }
        return args.stream();
    }

    public static List<String> getAllowedCurrencies() {
        return allowedCurrenciesStatic;
    }
}
