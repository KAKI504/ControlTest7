package org.example.controltest7.service;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency);
}