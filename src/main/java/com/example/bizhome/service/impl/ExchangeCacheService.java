package com.example.bizhome.service.impl;

import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.ExchangeSource;
import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExchangeCacheService implements ExchangeService {
    private final ExchangeSource exchangeSource;

    public ExchangeCacheService(ApplicationContext applicationContext) {
        this.exchangeSource = (ExchangeSource) applicationContext
                .getBean(applicationContext.getEnvironment().getProperty("exchange.source"));
    }

    @Cacheable(value = "EXCHANGE-VALUES", key = "{#currency, #date}")
    @Override
    synchronized public ExchangeSourceAnswer getValue(String currency, LocalDate date) {
        return exchangeSource.getValue(currency, date);
    }
}
