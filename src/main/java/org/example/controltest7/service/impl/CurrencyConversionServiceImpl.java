package org.example.controltest7.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.controltest7.service.CurrencyConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
    private final Map<String, Map<String, BigDecimal>> rates = new HashMap<>();

    public CurrencyConversionServiceImpl() {
        Map<String, BigDecimal> usdRates = new HashMap<>();
        usdRates.put("EUR", new BigDecimal("0.92"));  // 1 USD = 0.92 EUR
        usdRates.put("RUB", new BigDecimal("91.5"));  // 1 USD = 91.5 RUB

        Map<String, BigDecimal> eurRates = new HashMap<>();
        eurRates.put("USD", new BigDecimal("1.09"));  // 1 EUR = 1.09 USD
        eurRates.put("RUB", new BigDecimal("99.5"));  // 1 EUR = 99.5 RUB

        Map<String, BigDecimal> rubRates = new HashMap<>();
        rubRates.put("USD", new BigDecimal("0.0109"));  // 1 RUB = 0.0109 USD
        rubRates.put("EUR", new BigDecimal("0.0101"));  // 1 RUB = 0.0101 EUR

        rates.put("USD", usdRates);
        rates.put("EUR", eurRates);
        rates.put("RUB", rubRates);
    }

    @Override
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        Map<String, BigDecimal> currencyRates = rates.get(fromCurrency);
        if (currencyRates == null || !currencyRates.containsKey(toCurrency)) {
            log.error("Conversion rate not found for {} to {}", fromCurrency, toCurrency);
            throw new RuntimeException("Currency conversion not supported");
        }

        BigDecimal rate = currencyRates.get(toCurrency);
        log.info("Converting {} {} to {} at rate {}", amount, fromCurrency, toCurrency, rate);

        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}